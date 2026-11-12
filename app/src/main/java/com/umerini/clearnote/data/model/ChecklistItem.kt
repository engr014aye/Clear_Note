package com.umerini.clearnote.data.model

data class ChecklistItem(
    val id: String,
    val text: String,
    val isChecked: Boolean = false
) {
    companion object {
        fun serialize(items: List<ChecklistItem>): String {
            if (items.isEmpty()) return ""
            return items.joinToString("\n") { "${if (it.isChecked) "[x]" else "[ ]"}${it.text}" }
        }

        fun deserialize(raw: String): List<ChecklistItem> {
            if (raw.isBlank()) return emptyList()
            return raw.lines().filter { it.isNotBlank() }.mapIndexed { index, line ->
                val trimmed = line.trim()
                val isChecked = trimmed.startsWith("[x]", ignoreCase = true)
                val text = if (isChecked || trimmed.startsWith("[ ]")) {
                    trimmed.substring(3).trim()
                } else {
                    trimmed
                }
                ChecklistItem(id = index.toString(), text = text, isChecked = isChecked)
            }
        }
    }
}
