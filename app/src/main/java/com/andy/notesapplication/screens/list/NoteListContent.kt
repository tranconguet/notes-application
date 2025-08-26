package com.andy.notesapplication.screens.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.andy.model.Note
import com.andy.notesapplication.R
import com.andy.notesapplication.ui.components.ConfirmDialog
import com.andy.notesapplication.ui.components.NoteItem
import com.andy.notesapplication.ui.components.NoteListMessage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteListContent(
    pagedNotes: LazyPagingItems<Note>,
    snackBarHostState: SnackbarHostState,
    onNoteClick: (Long) -> Unit,
    onDeleteButtonClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    var deleteDialogState: NoteListDeleteDialogState by remember {
        mutableStateOf(NoteListDeleteDialogState.Hide)
    }

    when(val dialogState = deleteDialogState) {
        is NoteListDeleteDialogState.Show -> {
            ConfirmDialog(
                title = stringResource(R.string.delete_note),
                message = stringResource(R.string.do_you_want_to_delete_this_note),
                confirmText = stringResource(R.string.confirm),
                onConfirm = {
                    onDeleteButtonClick(dialogState.noteId)
                    deleteDialogState = NoteListDeleteDialogState.Hide
                },
                cancelText = stringResource(R.string.cancel),
                onDismiss = { deleteDialogState = NoteListDeleteDialogState.Hide }
            )
        }

        NoteListDeleteDialogState.Hide -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.your_notes),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(
                count = pagedNotes.itemCount,
                key = { index -> pagedNotes[index]?.id ?: index }
            ) { index ->
                val note = pagedNotes[index] ?: return@items
                NoteItem(
                    title = note.title,
                    content = note.content,
                    onClick = { onNoteClick(note.id) },
                    onLongClick = { deleteDialogState = NoteListDeleteDialogState.Show(note.id) }
                )
            }

            when (val loadingState = pagedNotes.loadState.append) {
                is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }

                is LoadState.NotLoading -> {
                    if (loadingState.endOfPaginationReached) {
                        if (pagedNotes.itemCount == 0) {
                            item {
                                NoteListMessage(stringResource(R.string.you_dont_have_any_note_please_add_one))
                            }
                        } else {
                            item {
                                NoteListMessage(stringResource(R.string.here_are_all_your_notes))
                            }
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        NoteListMessage(stringResource(R.string.something_went_wrong))
                    }
                }
            }
        }
    }
}