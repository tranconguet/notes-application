package com.andy.notesapplication.screens.list

sealed interface NoteListDeleteDialogState {
    data class Show(val noteId: Long): NoteListDeleteDialogState
    object Hide : NoteListDeleteDialogState
}