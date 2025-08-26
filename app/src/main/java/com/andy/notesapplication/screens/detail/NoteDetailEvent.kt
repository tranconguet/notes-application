package com.andy.notesapplication.screens.detail

sealed interface NoteDetailEvent {
    data class UpdateNote(val newTitle: String, val newContent: String) : NoteDetailEvent
    data class AddNote(val title: String, val content: String) : NoteDetailEvent
    object DeleteNote : NoteDetailEvent
}