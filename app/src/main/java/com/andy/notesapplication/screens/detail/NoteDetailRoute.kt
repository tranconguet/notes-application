package com.andy.notesapplication.screens.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val NOTE_DETAIL_ROUTE = "note_detail"
private const val ARG_NOTE_ID = "note_id"

fun NavGraphBuilder.noteDetailRoute(
    navController: NavHostController,
) {
    composable(
        "$NOTE_DETAIL_ROUTE/{$ARG_NOTE_ID}",
        arguments = listOf(navArgument(ARG_NOTE_ID) { type = NavType.LongType }),
        content = { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong(ARG_NOTE_ID).takeIf { it != -1L }
            NoteDetailScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() }
            )
        }
    )
}