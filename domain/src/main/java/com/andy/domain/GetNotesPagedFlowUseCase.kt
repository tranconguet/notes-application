package com.andy.domain

import androidx.paging.PagingData
import com.andy.data.repository.NoteRepository
import com.andy.model.Note
import kotlinx.coroutines.flow.Flow

class GetNotesPagedFlowUseCase(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<PagingData<Note>> =
        noteRepository.getNotesPagedFlow()
}