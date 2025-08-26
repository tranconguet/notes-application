package com.andy.domain

import com.andy.data.repository.NoteRepository

class DeleteNoteUseCase(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = noteRepository.delete(id)
}