package com.andy.domain

import com.andy.data.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setup() {
        noteRepository = mockk()
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    }

    @Test
    fun `when repository returns success, use case should return success`() = runTest {
        val noteId = 1L
        coEvery { noteRepository.delete(noteId) } returns Result.success(Unit)

        val result = deleteNoteUseCase(noteId)

        assertEquals(Result.success(Unit), result)
        coVerify(exactly = 1) { noteRepository.delete(noteId) }
    }

    @Test
    fun `when repository returns failure, use case should return failure`() = runTest {
        val noteId = 2L
        val exception = RuntimeException("Delete failed")
        coEvery { noteRepository.delete(noteId) } returns Result.failure(exception)

        val result = deleteNoteUseCase(noteId)

        assertEquals(Result.failure<Unit>(exception), result)
        coVerify(exactly = 1) { noteRepository.delete(noteId) }
    }
}
