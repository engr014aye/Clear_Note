package com.umerini.clearnote.data.repository

import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.data.model.NoteCategory
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getActiveNotes(): Flow<List<Note>>
    fun getArchivedNotes(): Flow<List<Note>>
    fun getNotesByCategory(category: NoteCategory): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    fun getNoteById(id: Long): Flow<Note?>
    suspend fun getNoteByIdSuspend(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNoteById(id: Long)
    suspend fun togglePin(id: Long, isPinned: Boolean)
    suspend fun toggleArchive(id: Long, isArchived: Boolean)
    fun getActiveNotesCount(): Flow<Int>
}
