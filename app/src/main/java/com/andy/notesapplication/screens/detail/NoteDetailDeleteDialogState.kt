package com.andy.notesapplication.screens.detail

sealed interface NoteDetailDeleteDialogState {
    object Show: NoteDetailDeleteDialogState
    object Hide : NoteDetailDeleteDialogState
}