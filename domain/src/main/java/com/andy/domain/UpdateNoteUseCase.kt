package com.andy.domain

import com.andy.data.repository.NoteRepository

class UpdateNoteUseCase(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Long, title: String, content: String): Result<Unit> =
        noteRepository.update(id, title, content)
}