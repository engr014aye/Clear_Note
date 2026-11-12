package com.umerini.clearnote.ui.notes

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umerini.clearnote.data.model.NoteCategory
import com.umerini.clearnote.ui.components.LiquidGlassPill

@Composable
fun CategorySelectorRow(
    selectedCategory: NoteCategory,
    onSelectCategory: (NoteCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NoteCategory.entries.forEach { category ->
            LiquidGlassPill(
                label = category.displayName,
                isSelected = selectedCategory == category,
                onClick = { onSelectCategory(category) }
            )
        }
    }
}
