package com.andy.notesapplication.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andy.notesapplication.R
import com.andy.notesapplication.ui.components.ConfirmDialog
import com.andy.notesapplication.utils.safeClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailContent(
    noteDetailState: NoteDetailUiState,
    snackBarHostState: SnackbarHostState,
    updateNote: (title: String, content: String) -> Unit,
    addNote: (title: String, content: String) -> Unit,
    onDeleteNote: () -> Unit,
    showEmptyTitleMessage: () -> Unit,
    navigateBack: () -> Unit
) {
    val currentNote = when (noteDetailState) {
        is NoteDetailUiState.Success -> noteDetailState.note
        else -> null
    }

    var title by remember { mutableStateOf(currentNote?.title ?: "") }
    var content by remember { mutableStateOf(currentNote?.content ?: "") }

    var deleteDialogState: NoteDetailDeleteDialogState by remember {
        mutableStateOf(NoteDetailDeleteDialogState.Hide)
    }

    when (deleteDialogState) {
        is NoteDetailDeleteDialogState.Show -> {
            ConfirmDialog(
                title = stringResource(R.string.delete_note),
                message = stringResource(R.string.do_you_want_to_delete_this_note),
                confirmText = stringResource(R.string.confirm),
                onConfirm = {
                    onDeleteNote()
                    deleteDialogState = NoteDetailDeleteDialogState.Hide
                },
                cancelText = stringResource(R.string.cancel),
                onDismiss = { deleteDialogState = NoteDetailDeleteDialogState.Hide }
            )
        }

        NoteDetailDeleteDialogState.Hide -> {}
    }

    LaunchedEffect(currentNote) {
        title = currentNote?.title.orEmpty()
        content = currentNote?.content.orEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold
                        ),
                        enabled = noteDetailState is NoteDetailUiState.New || noteDetailState is NoteDetailUiState.Success,
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    stringResource(R.string.un_title),
                                    style = LocalTextStyle.current.copy(
                                        color = Color.Black,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Thin
                                    )
                                )
                            }
                            innerTextField()
                        })
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .safeClickable(onClick = navigateBack),
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentNote != null) {
                                updateNote(title, content)
                            } else {
                                if (title.isEmpty()) {
                                    showEmptyTitleMessage()
                                } else {
                                    addNote(title, content)
                                }
                            }
                        }) {
                        Icon(Icons.Default.Done, contentDescription = "Save")
                    }
                    if (noteDetailState is NoteDetailUiState.Success) {
                        IconButton(
                            onClick = {
                                deleteDialogState = NoteDetailDeleteDialogState.Show
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (noteDetailState) {
                NoteDetailUiState.Error -> {
                    Text(stringResource(R.string.cannot_load_note_detail))
                }

                NoteDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is NoteDetailUiState.Success, NoteDetailUiState.New -> {
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text(stringResource(R.string.content)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        maxLines = Int.MAX_VALUE
                    )
                }
            }
        }
    }
}