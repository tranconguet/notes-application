package com.andy.domain

import com.andy.data.repository.NoteRepository
import com.andy.model.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var createNoteUseCase: CreateNoteUseCase

    @Before
    fun setup() {
        noteRepository = mockk()
        createNoteUseCase = CreateNoteUseCase(noteRepository)
    }

    @Test
    fun `when repository returns success, use case should return success`() = runTest {
        // Given
        val note = Note(id = 1, title = "Test", content = "Content", updatedAt = 1L)
        coEvery { noteRepository.create(note) } returns Result.success(Unit)

        // When
        val result = createNoteUseCase(note)

        // Then
        assertEquals(Result.success(Unit), result)
        coVerify(exactly = 1) { noteRepository.create(note) }
    }

    @Test
    fun `when repository returns failure, use case should return failure`() = runTest {
        // Given
        val note = Note(id = 2, title = "Fail", content = "Content", updatedAt = 1L)
        val exception = RuntimeException("DB error")
        coEvery { noteRepository.create(note) } returns Result.failure(exception)

        // When
        val result = createNoteUseCase(note)

        // Then
        assertEquals(Result.failure<Unit>(exception), result)
        coVerify(exactly = 1) { noteRepository.create(note) }
    }
}