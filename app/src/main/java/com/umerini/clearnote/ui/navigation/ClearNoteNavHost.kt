package com.umerini.clearnote.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umerini.clearnote.data.repository.NoteRepository
import com.umerini.clearnote.ui.editor.NoteEditorScreen
import com.umerini.clearnote.ui.editor.NoteEditorViewModel
import com.umerini.clearnote.ui.notes.NotesListScreen
import com.umerini.clearnote.ui.notes.NotesViewModel

object Screen {
    const val NOTES_LIST = "notes_list"
    const val NOTE_EDITOR = "note_editor/{noteId}"

    fun noteEditorRoute(noteId: Long = 0L): String = "note_editor/$noteId"
}

@Composable
fun ClearNoteNavHost(
    navController: NavHostController,
    repository: NoteRepository,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NOTES_LIST,
        modifier = modifier
    ) {
        composable(
            route = Screen.NOTES_LIST,
            enterTransition = {
                fadeIn(animationSpec = tween(250))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            }
        ) {
            val notesViewModel: NotesViewModel = viewModel(
                factory = NotesViewModel.provideFactory(repository)
            )
            NotesListScreen(
                viewModel = notesViewModel,
                onNavigateToEditor = { noteId ->
                    navController.navigate(Screen.noteEditorRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.NOTE_EDITOR,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(250))
            }
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            val editorViewModel: NoteEditorViewModel = viewModel(
                key = "editor_$noteId",
                factory = NoteEditorViewModel.provideFactory(repository, noteId)
            )
            NoteEditorScreen(
                viewModel = editorViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
