package com.umerini.clearnote

import android.app.Application
import com.umerini.clearnote.data.local.ClearNoteDatabase
import com.umerini.clearnote.data.repository.NoteRepository
import com.umerini.clearnote.data.repository.NoteRepositoryImpl

class ClearNoteApplication : Application() {

    lateinit var noteRepository: NoteRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = ClearNoteDatabase.getInstance(this)
        noteRepository = NoteRepositoryImpl(database.noteDao())
    }
}
