package com.andy.notesapplication.screens.detail

sealed interface NoteDetailEffect {
    data object NavigateBack : NoteDetailEffect
    data object ShowSaveFailedMessage : NoteDetailEffect
    data object ShowAddFailedMessage : NoteDetailEffect
    data object ShowDeleteFailedMessage : NoteDetailEffect
}