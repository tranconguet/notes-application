package com.andy.domain

import com.andy.data.repository.NoteRepository
import com.andy.model.Note

class CreateNoteUseCase(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Unit> = noteRepository.create(note)
}