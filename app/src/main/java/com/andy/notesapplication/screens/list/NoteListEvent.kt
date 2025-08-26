package com.andy.notesapplication.screens.list

sealed interface NoteListEvent {
    data class DeleteNote(val id: Long) : NoteListEvent
}