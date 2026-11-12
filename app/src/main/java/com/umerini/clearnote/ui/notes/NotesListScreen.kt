package com.umerini.clearnote.ui.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umerini.clearnote.data.model.Note
import com.umerini.clearnote.ui.components.LiquidGlassConfirmDialog
import com.umerini.clearnote.ui.components.LiquidGlassFab
import com.umerini.clearnote.ui.components.LiquidGlassInfoDialog
import com.umerini.clearnote.ui.components.LiquidGlassNoteCard
import com.umerini.clearnote.ui.components.LiquidGlassSearchBar
import com.umerini.clearnote.ui.components.LiquidGlassTopBar
import com.umerini.clearnote.ui.theme.BackgroundGradient
import com.umerini.clearnote.ui.theme.bouncingOverscroll

@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    onNavigateToEditor: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var noteForActionMenu by remember { mutableStateOf<Note?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Liquid Glass Top Bar
            LiquidGlassTopBar(
                isGridView = uiState.isGridView,
                isSearchVisible = uiState.isSearchVisible,
                onToggleSearch = { viewModel.toggleSearchVisibility() },
                onToggleView = { viewModel.toggleViewMode() },
                onOpenInfo = { viewModel.showInfoDialog() }
            )

            // Animated Frosted Search Bar
            AnimatedVisibility(
                visible = uiState.isSearchVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LiquidGlassSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Categories Selector Pills
            CategorySelectorRow(
                selectedCategory = uiState.selectedCategory,
                onSelectCategory = { viewModel.selectCategory(it) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Main Content Area (Empty state or Note items with Apple bouncing overscroll)
            if (uiState.notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyNotesState(isSearching = uiState.searchQuery.isNotBlank())
                }
            } else {
                if (uiState.isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 96.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .bouncingOverscroll()
                    ) {
                        items(
                            items = uiState.notes,
                            key = { it.id }
                        ) { note ->
                            LiquidGlassNoteCard(
                                note = note,
                                onClick = { onNavigateToEditor(note.id) },
                                onLongClick = { noteForActionMenu = note }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .bouncingOverscroll()
                    ) {
                        items(
                            items = uiState.notes,
                            key = { it.id }
                        ) { note ->
                            LiquidGlassNoteCard(
                                note = note,
                                onClick = { onNavigateToEditor(note.id) },
                                onLongClick = { noteForActionMenu = note }
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            LiquidGlassFab(
                onClick = { onNavigateToEditor(0L) },
                label = "New Note"
            )
        }

        // Context Action Dialog for long-pressed note
        noteForActionMenu?.let { note ->
            LiquidGlassConfirmDialog(
                title = if (note.title.isNotBlank()) note.title else "Note Options",
                message = if (note.isPinned) "Unpin this note from the top or delete it?" else "Pin this note to the top or delete it?",
                confirmText = if (note.isPinned) "Unpin Note" else "Pin to Top",
                isDestructive = false,
                onConfirm = {
                    viewModel.togglePin(note)
                    noteForActionMenu = null
                },
                onDismiss = {
                    noteForActionMenu = null
                }
            )
        }

        // Delete Confirmation Dialog
        uiState.noteToDelete?.let { note ->
            LiquidGlassConfirmDialog(
                title = "Delete Note?",
                message = "Are you sure you want to delete \"${if (note.title.isNotBlank()) note.title else "Untitled Note"}\"? This action cannot be undone.",
                confirmText = "Delete Permanently",
                isDestructive = true,
                onConfirm = { viewModel.confirmDelete() },
                onDismiss = { viewModel.dismissDeletePrompt() }
            )
        }

        // Privacy & Info Dialog
        if (uiState.isInfoDialogVisible) {
            LiquidGlassInfoDialog(
                totalNotes = uiState.totalNotesCount,
                onDismiss = { viewModel.dismissInfoDialog() }
            )
        }
    }
}
