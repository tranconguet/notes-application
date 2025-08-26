package com.andy.notesapplication.screens.detail

import com.andy.domain.CreateNoteUseCase
import com.andy.domain.DeleteNoteUseCase
import com.andy.domain.GetNoteFlowUseCase
import com.andy.domain.UpdateNoteUseCase
import com.andy.model.Note
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteDetailViewModelTest {

    private lateinit var getNoteFlowUseCase: GetNoteFlowUseCase
    private lateinit var createNoteUseCase: CreateNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    private lateinit var viewModel: NoteDetailViewModel

    @Before
    fun setup() {
        getNoteFlowUseCase = mockk()
        createNoteUseCase = mockk()
        updateNoteUseCase = mockk()
        deleteNoteUseCase = mockk()
    }

    @Test
    fun `uiState emits Loading then Success when note found`() = runTest {
        // Arrange
        every { getNoteFlowUseCase(1L) } returns flowOf(
            Note(1, "Title", "Content", updatedAt = 1L)
        )

        viewModel = NoteDetailViewModel(
            noteId = 1L,
            getNoteFlowUseCase = getNoteFlowUseCase,
            createNoteUseCase = createNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )

        // Act
        val states = viewModel.uiState.take(2).toList() // collect first 2 states

        // Assert
        assertEquals(
            listOf(
                NoteDetailUiState.Loading,
                NoteDetailUiState.Success(
                    Note(1, "Title", "Content", updatedAt = 1L)
                )
            ),
            states
        )
    }

    @Test
    fun `uiState emits Loading then Error when note not found`() = runTest {
        // Arrange
        every { getNoteFlowUseCase(1L) } returns flowOf(null)

        viewModel = NoteDetailViewModel(
            noteId = 1L,
            getNoteFlowUseCase = getNoteFlowUseCase,
            createNoteUseCase = createNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )

        // Act
        val states = viewModel.uiState.take(2).toList()

        // Assert
        assertEquals(
            listOf(
                NoteDetailUiState.Loading,
                NoteDetailUiState.Error
            ),
            states
        )
    }

    @Test
    fun `onEvent AddNote emits NavigateBack on success`() = runTest {
        coEvery { createNoteUseCase(any()) } returns Result.success(Unit)

        viewModel = NoteDetailViewModel(
            noteId = null,
            getNoteFlowUseCase = getNoteFlowUseCase,
            createNoteUseCase = createNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )

        viewModel.onEvent(NoteDetailEvent.AddNote("title", "content"))

        val effects = viewModel.effect.take(1).toList()
        assertEquals(listOf(NoteDetailEffect.NavigateBack), effects)
    }

    @Test
    fun `onEvent UpdateNote emits ShowSaveFailedMessage on failure`() = runTest {
        coEvery { updateNoteUseCase(any(), any(), any()) } returns Result.failure(Exception("fail"))
        every { getNoteFlowUseCase(1L) } returns flowOf(
            Note(1, "Title", "Content", updatedAt = 1L)
        )

        viewModel = NoteDetailViewModel(
            noteId = 1L,
            getNoteFlowUseCase = getNoteFlowUseCase,
            createNoteUseCase = createNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )

        viewModel.onEvent(NoteDetailEvent.UpdateNote("title", "content"))

        val effects = viewModel.effect.take(1).toList()
        assertEquals(listOf(NoteDetailEffect.ShowSaveFailedMessage), effects)
    }

    @Test
    fun `onEvent DeleteNote emits NavigateBack on success`() = runTest {
        coEvery { deleteNoteUseCase(1L) } returns Result.success(Unit)
        every { getNoteFlowUseCase(1L) } returns flowOf(
            Note(1, "Title", "Content", updatedAt = 1L)
        )

        viewModel = NoteDetailViewModel(
            noteId = 1L,
            getNoteFlowUseCase = getNoteFlowUseCase,
            createNoteUseCase = createNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )

        viewModel.onEvent(NoteDetailEvent.DeleteNote)

        val effects = viewModel.effect.take(1).toList()
        assertEquals(listOf(NoteDetailEffect.NavigateBack), effects)
    }
}