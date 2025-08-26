package com.andy.notesapplication.screens.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.andy.notesapplication.R
import com.andy.notesapplication.ServiceLocator
import com.andy.notesapplication.utils.dismissAndShow
import com.andy.notesapplication.utils.rememberFlowWithLifecycle

@Composable
fun NoteListScreen(
    navigateToNoteDetail: (Long) -> Unit,
    navigateToNewNote: () -> Unit
) {
    val viewModel: NoteListViewModel = viewModel(
        factory = NoteListViewModelFactory(ServiceLocator.provideNoteRepository(LocalContext.current))
    )
    val effect = rememberFlowWithLifecycle(viewModel.effect)
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                NoteListEffect.ShowDeleteFailedMessage -> {
                    snackBarHostState.dismissAndShow(
                        message = context.getString(R.string.delete_note_failed),
                        coroutineScope = scope
                    )
                }
                NoteListEffect.ShowDeleteSuccessMessage -> {
                    snackBarHostState.dismissAndShow(
                        message = context.getString(R.string.delete_note_successfully),
                        coroutineScope = scope
                    )
                }
            }
        }
    }

    val pagedNotes = viewModel.pagedNotes.collectAsLazyPagingItems()
    NoteListContent(
        pagedNotes = pagedNotes,
        snackBarHostState = snackBarHostState,
        onNoteClick = navigateToNoteDetail,
        onAddClick = navigateToNewNote,
        onDeleteButtonClick = {
            viewModel.onEvent(NoteListEvent.DeleteNote(it))
        }
    )
}
