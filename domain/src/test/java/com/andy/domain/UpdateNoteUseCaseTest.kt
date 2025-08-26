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
class UpdateNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setup() {
        noteRepository = mockk()
        updateNoteUseCase = UpdateNoteUseCase(noteRepository)
    }

    @Test
    fun `when repository returns success, usecase should return success`() = runTest {
        val noteId = 1L
        val title = "Updated Title"
        val content = "Updated Content"
        coEvery { noteRepository.update(noteId, title, content) } returns Result.success(Unit)

        val result = updateNoteUseCase(noteId, title, content)

        assertEquals(Result.success(Unit), result)
        coVerify(exactly = 1) { noteRepository.update(noteId, title, content) }
    }

    @Test
    fun `when repository returns failure, usecase should return failure`() = runTest {
        val noteId = 2L
        val title = "Fail Title"
        val content = "Fail Content"
        val exception = RuntimeException("Update failed")
        coEvery { noteRepository.update(noteId, title, content) } returns Result.failure(exception)

        val result = updateNoteUseCase(noteId, title, content)

        assertEquals(Result.failure<Unit>(exception), result)
        coVerify(exactly = 1) { noteRepository.update(noteId, title, content) }
    }
}