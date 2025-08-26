package com.andy.notesapplication.screens.detail

import com.andy.model.Note

sealed interface NoteDetailUiState {
    data object Loading : NoteDetailUiState
    data class Success(val note: Note) : NoteDetailUiState
    data object Error : NoteDetailUiState
    data object New : NoteDetailUiState
}