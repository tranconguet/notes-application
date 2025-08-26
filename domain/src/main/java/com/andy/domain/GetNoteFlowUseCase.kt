package com.andy.domain

import com.andy.data.repository.NoteRepository
import com.andy.model.Note
import kotlinx.coroutines.flow.Flow

class GetNoteFlowUseCase(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: Long): Flow<Note?> = noteRepository.getNote(id)
}