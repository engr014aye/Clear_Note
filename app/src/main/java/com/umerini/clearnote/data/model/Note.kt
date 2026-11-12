package com.umerini.clearnote.data.model

data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val category: NoteCategory = NoteCategory.ALL,
    val colorIndex: Int = 0,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val checklistItems: List<ChecklistItem> = emptyList()
) {
    val wordCount: Int
        get() {
            val text = "$title $content ${checklistItems.joinToString(" ") { it.text }}"
            return text.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
        }

    val charCount: Int
        get() = title.length + content.length + checklistItems.sumOf { it.text.length }

    val isChecklist: Boolean
        get() = checklistItems.isNotEmpty()

    val completedChecklistCount: Int
        get() = checklistItems.count { it.isChecked }
}
