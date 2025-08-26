package com.andy.notesapplication.screens.list

sealed interface NoteListEffect {
    data object ShowDeleteSuccessMessage : NoteListEffect
    data object ShowDeleteFailedMessage : NoteListEffect
}