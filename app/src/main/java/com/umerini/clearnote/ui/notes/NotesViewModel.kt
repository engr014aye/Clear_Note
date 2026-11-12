package com.umerini.clearnote.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.data.model.NoteCategory
import com.umerini.clearnote.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val selectedCategory: NoteCategory = NoteCategory.ALL,
    val searchQuery: String = "",
    val isGridView: Boolean = true,
    val isSearchVisible: Boolean = false,
    val totalNotesCount: Int = 0,
    val noteToDelete: Note? = null,
    val isInfoDialogVisible: Boolean = false
)

private data class UiFlags(
    val isGrid: Boolean,
    val isSearch: Boolean,
    val toDelete: Note?,
    val isInfo: Boolean,
    val count: Int
)

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(NoteCategory.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _isGridView = MutableStateFlow(true)
    private val _isSearchVisible = MutableStateFlow(false)
    private val _noteToDelete = MutableStateFlow<Note?>(null)
    private val _isInfoDialogVisible = MutableStateFlow(false)

    private val _notes = combine(_selectedCategory, _searchQuery) { category, query ->
        Pair(category, query)
    }.flatMapLatest { (category, query) ->
        if (query.isNotBlank()) {
            repository.searchNotes(query)
        } else {
            repository.getNotesByCategory(category)
        }
    }

    private val _flags = combine(
        _isGridView,
        _isSearchVisible,
        _noteToDelete,
        _isInfoDialogVisible,
        repository.getActiveNotesCount()
    ) { isGrid, isSearch, toDelete, isInfo, count ->
        UiFlags(
            isGrid = isGrid,
            isSearch = isSearch,
            toDelete = toDelete,
            isInfo = isInfo,
            count = count
        )
    }

    val uiState: StateFlow<NotesUiState> = combine(
        _notes,
        _selectedCategory,
        _searchQuery,
        _flags
    ) { notes, category, query, flags ->
        NotesUiState(
            notes = notes,
            selectedCategory = category,
            searchQuery = query,
            isGridView = flags.isGrid,
            isSearchVisible = flags.isSearch,
            totalNotesCount = flags.count,
            noteToDelete = flags.toDelete,
            isInfoDialogVisible = flags.isInfo
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState()
    )

    fun selectCategory(category: NoteCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearchVisibility() {
        _isSearchVisible.value = !_isSearchVisible.value
        if (!_isSearchVisible.value) {
            _searchQuery.value = ""
        }
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            repository.togglePin(note.id, !note.isPinned)
        }
    }

    fun toggleArchive(note: Note) {
        viewModelScope.launch {
            repository.toggleArchive(note.id, !note.isArchived)
        }
    }

    fun promptDelete(note: Note) {
        _noteToDelete.value = note
    }

    fun dismissDeletePrompt() {
        _noteToDelete.value = null
    }

    fun confirmDelete() {
        val note = _noteToDelete.value ?: return
        viewModelScope.launch {
            repository.deleteNote(note)
            _noteToDelete.value = null
        }
    }

    fun showInfoDialog() {
        _isInfoDialogVisible.value = true
    }

    fun dismissInfoDialog() {
        _isInfoDialogVisible.value = false
    }

    companion object {
        fun provideFactory(repository: NoteRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(repository) as T
                }
            }
    }
}
