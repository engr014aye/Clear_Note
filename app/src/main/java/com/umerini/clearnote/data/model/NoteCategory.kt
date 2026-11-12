package com.umerini.clearnote.data.model

enum class NoteCategory(val displayName: String, val iconName: String) {
    ALL("All", "Category"),
    PERSONAL("Personal", "Person"),
    WORK("Work", "Work"),
    IDEAS("Ideas", "Lightbulb"),
    TASKS("Tasks", "CheckCircle"),
    ARCHIVE("Archive", "Archive");

    companion object {
        fun fromString(value: String): NoteCategory {
            return entries.find { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) } ?: ALL
        }
    }
}
