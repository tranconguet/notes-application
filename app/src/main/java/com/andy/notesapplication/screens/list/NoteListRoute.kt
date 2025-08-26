package com.andy.notesapplication.screens.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.andy.notesapplication.screens.detail.NOTE_DETAIL_ROUTE

const val NOTE_LIST_ROUTE = "note_list"

fun NavGraphBuilder.noteListRoute(
    navController: NavHostController,
) {
    composable(NOTE_LIST_ROUTE) {
        NoteListScreen(
            navigateToNoteDetail = { noteId ->
                navController.navigate("$NOTE_DETAIL_ROUTE/$noteId")
            },
            navigateToNewNote = {
                navController.navigate("$NOTE_DETAIL_ROUTE/-1")
            }
        )
    }
}