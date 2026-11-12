package com.umerini.clearnote.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.umerini.clearnote.data.model.ChecklistItem
import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.data.model.NoteCategory
import com.umerini.clearnote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NoteEditorUiState(
    val noteId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val category: NoteCategory = NoteCategory.ALL,
    val colorIndex: Int = 0,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val isChecklistMode: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
) {
    val wordCount: Int
        get() {
            val fullText = "$title $content ${checklistItems.joinToString(" ") { it.text }}"
            return fullText.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
        }

    val charCount: Int
        get() = title.length + content.length + checklistItems.sumOf { it.text.length }

    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
            return sdf.format(Date(lastUpdated))
        }
}

class NoteEditorViewModel(
    private val repository: NoteRepository,
    private val initialNoteId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState(noteId = initialNoteId))
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    init {
        if (initialNoteId > 0L) {
            loadNote(initialNoteId)
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            val note = repository.getNoteByIdSuspend(id)
            if (note != null) {
                _uiState.value = NoteEditorUiState(
                    noteId = note.id,
                    title = note.title,
                    content = note.content,
                    category = note.category,
                    colorIndex = note.colorIndex,
                    isPinned = note.isPinned,
                    isArchived = note.isArchived,
                    checklistItems = note.checklistItems,
                    isChecklistMode = note.isChecklist,
                    lastUpdated = note.updatedAt
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
        autoSave()
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
        autoSave()
    }

    fun setCategory(category: NoteCategory) {
        _uiState.value = _uiState.value.copy(category = category)
        autoSave()
    }

    fun setColorIndex(index: Int) {
        _uiState.value = _uiState.value.copy(colorIndex = index)
        autoSave()
    }

    fun togglePin() {
        val newPinned = !_uiState.value.isPinned
        _uiState.value = _uiState.value.copy(isPinned = newPinned)
        autoSave()
    }

    fun toggleChecklistMode() {
        val current = _uiState.value
        val newMode = !current.isChecklistMode
        val items = if (newMode && current.checklistItems.isEmpty() && current.content.isNotBlank()) {
            current.content.lines().filter { it.isNotBlank() }.mapIndexed { i, line ->
                ChecklistItem(id = i.toString(), text = line.trim(), isChecked = false)
            }
        } else {
            current.checklistItems
        }
        _uiState.value = current.copy(isChecklistMode = newMode, checklistItems = items)
        autoSave()
    }

    fun addChecklistItem(text: String = "") {
        val current = _uiState.value
        val newItem = ChecklistItem(
            id = System.currentTimeMillis().toString(),
            text = text,
            isChecked = false
        )
        _uiState.value = current.copy(checklistItems = current.checklistItems + newItem)
        autoSave()
    }

    fun updateChecklistItem(index: Int, newText: String) {
        val current = _uiState.value
        if (index in current.checklistItems.indices) {
            val updated = current.checklistItems.toMutableList()
            updated[index] = updated[index].copy(text = newText)
            _uiState.value = current.copy(checklistItems = updated)
            autoSave()
        }
    }

    fun toggleChecklistItem(index: Int) {
        val current = _uiState.value
        if (index in current.checklistItems.indices) {
            val updated = current.checklistItems.toMutableList()
            val item = updated[index]
            updated[index] = item.copy(isChecked = !item.isChecked)
            _uiState.value = current.copy(checklistItems = updated)
            autoSave()
        }
    }

    fun removeChecklistItem(index: Int) {
        val current = _uiState.value
        if (index in current.checklistItems.indices) {
            val updated = current.checklistItems.toMutableList()
            updated.removeAt(index)
            _uiState.value = current.copy(checklistItems = updated)
            autoSave()
        }
    }

    private fun autoSave() {
        val current = _uiState.value
        // Only save if there is content or title
        if (current.title.isBlank() && current.content.isBlank() && current.checklistItems.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val noteToSave = Note(
                id = current.noteId,
                title = current.title,
                content = current.content,
                category = current.category,
                colorIndex = current.colorIndex,
                isPinned = current.isPinned,
                isArchived = current.isArchived,
                updatedAt = System.currentTimeMillis(),
                checklistItems = if (current.isChecklistMode) current.checklistItems else emptyList()
            )

            if (current.noteId == 0L) {
                val newId = repository.insertNote(noteToSave)
                _uiState.value = _uiState.value.copy(noteId = newId)
            } else {
                repository.updateNote(noteToSave)
            }
        }
    }

    fun deleteNote() {
        val current = _uiState.value
        if (current.noteId > 0L) {
            viewModelScope.launch {
                repository.deleteNoteById(current.noteId)
                _uiState.value = current.copy(isDeleted = true)
            }
        } else {
            _uiState.value = current.copy(isDeleted = true)
        }
    }

    companion object {
        fun provideFactory(
            repository: NoteRepository,
            noteId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteEditorViewModel(repository, noteId) as T
            }
        }
    }
}
