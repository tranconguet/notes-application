package com.andy.notesapplication.screens.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andy.notesapplication.R
import com.andy.notesapplication.ServiceLocator
import com.andy.notesapplication.utils.dismissAndShow
import com.andy.notesapplication.utils.rememberFlowWithLifecycle

@Composable
fun NoteDetailScreen(
    noteId: Long?,
    navigateBack: () -> Unit
) {
    val viewModel: NoteDetailViewModel = viewModel(
        factory = NoteDetailViewModelFactory(
            noteId = noteId,
            ServiceLocator.provideNoteRepository(LocalContext.current)
        )
    )

    val noteDetailState by viewModel.uiState.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effect)
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                NoteDetailEffect.ShowAddFailedMessage -> {
                    snackBarHostState.dismissAndShow(
                        message = context.getString(R.string.add_note_failed),
                        coroutineScope = scope
                    )
                }

                NoteDetailEffect.ShowSaveFailedMessage -> {
                    snackBarHostState.dismissAndShow(
                        message = context.getString(R.string.save_note_failed),
                        coroutineScope = scope
                    )
                }

                NoteDetailEffect.ShowDeleteFailedMessage -> {
                    snackBarHostState.dismissAndShow(
                        message = context.getString(R.string.delete_note_failed),
                        coroutineScope = scope
                    )
                }

                NoteDetailEffect.NavigateBack -> {
                    navigateBack()
                }

            }
        }
    }

    NoteDetailContent(
        noteDetailState = noteDetailState,
        snackBarHostState = snackBarHostState,
        updateNote = { title, content ->
            viewModel.onEvent(NoteDetailEvent.UpdateNote(newTitle = title, newContent = content))
        },
        addNote = { title, content ->
            viewModel.onEvent(NoteDetailEvent.AddNote(title = title, content = content))
        },
        onDeleteNote = {
            viewModel.onEvent(NoteDetailEvent.DeleteNote)
        },
        showEmptyTitleMessage = {
            snackBarHostState.dismissAndShow(
                message = context.getString(R.string.title_cannot_be_empty),
                coroutineScope = scope
            )
        },
        navigateBack = navigateBack
    )
}
