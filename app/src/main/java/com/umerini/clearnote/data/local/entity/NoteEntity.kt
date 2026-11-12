package com.umerini.clearnote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.umerini.clearnote.data.model.ChecklistItem
import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.data.model.NoteCategory

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String,
    val colorIndex: Int,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val checklistData: String = ""
) {
    fun toDomainModel(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            category = NoteCategory.fromString(category),
            colorIndex = colorIndex,
            isPinned = isPinned,
            isArchived = isArchived,
            createdAt = createdAt,
            updatedAt = updatedAt,
            checklistItems = ChecklistItem.deserialize(checklistData)
        )
    }

    companion object {
        fun fromDomainModel(note: Note): NoteEntity {
            return NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                category = note.category.name,
                colorIndex = note.colorIndex,
                isPinned = note.isPinned,
                isArchived = note.isArchived,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                checklistData = ChecklistItem.serialize(note.checklistItems)
            )
        }
    }
}
