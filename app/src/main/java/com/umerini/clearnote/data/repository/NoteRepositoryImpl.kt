package com.umerini.clearnote.data.repository

import com.umerini.clearnote.data.local.NoteDao
import com.umerini.clearnote.data.local.entity.NoteEntity
import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.data.model.NoteCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getActiveNotes(): Flow<List<Note>> {
        return noteDao.getActiveNotes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getArchivedNotes(): Flow<List<Note>> {
        return noteDao.getArchivedNotes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getNotesByCategory(category: NoteCategory): Flow<List<Note>> {
        return if (category == NoteCategory.ALL) {
            getActiveNotes()
        } else if (category == NoteCategory.ARCHIVE) {
            getArchivedNotes()
        } else {
            noteDao.getNotesByCategory(category.name).map { entities ->
                entities.map { it.toDomainModel() }
            }
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getNoteById(id: Long): Flow<Note?> {
        return noteDao.getNoteById(id).map { it?.toDomainModel() }
    }

    override suspend fun getNoteByIdSuspend(id: Long): Note? {
        return noteDao.getNoteByIdSuspend(id)?.toDomainModel()
    }

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(NoteEntity.fromDomainModel(note))
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(NoteEntity.fromDomainModel(note))
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(NoteEntity.fromDomainModel(note))
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun togglePin(id: Long, isPinned: Boolean) {
        noteDao.updatePinStatus(id, isPinned)
    }

    override suspend fun toggleArchive(id: Long, isArchived: Boolean) {
        noteDao.updateArchiveStatus(id, isArchived)
    }

    override fun getActiveNotesCount(): Flow<Int> {
        return noteDao.getActiveNotesCount()
    }
}
