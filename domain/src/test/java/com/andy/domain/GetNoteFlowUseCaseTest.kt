package com.andy.domain

import com.andy.data.repository.NoteRepository
import com.andy.model.Note
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNoteFlowUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var getNoteFlowUseCase: GetNoteFlowUseCase

    @Before
    fun setup() {
        noteRepository = mockk()
        getNoteFlowUseCase = GetNoteFlowUseCase(noteRepository)
    }

    @Test
    fun `when repository emits a note, use case should emit the same note`() = runTest {
        val noteId = 1L
        val note = Note(id = noteId, title = "Title", content = "Content", updatedAt = 1L)
        every { noteRepository.getNote(noteId) } returns flowOf(note)

        val result = getNoteFlowUseCase(noteId).first()
        assertEquals(note, result)
    }

    @Test
    fun `when repository emits null, use case should emit null`() = runTest {
        val noteId = 2L
        every { noteRepository.getNote(noteId) } returns flowOf(null)

        val result = getNoteFlowUseCase(noteId).first()
        assertEquals(null, result)
    }
}