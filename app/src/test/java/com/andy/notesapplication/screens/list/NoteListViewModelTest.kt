package com.andy.notesapplication.screens.list

import androidx.paging.PagingData
import com.andy.domain.DeleteNoteUseCase
import com.andy.domain.GetNotesPagedFlowUseCase
import io.mockk.coEvery
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
class NoteListViewModelTest {

    private lateinit var getNotesPagedFlowUseCase: GetNotesPagedFlowUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var viewModel: NoteListViewModel

    @Before
    fun setup() {
        getNotesPagedFlowUseCase = mockk()
        deleteNoteUseCase = mockk()

        // always return empty PagingData for pagedNotes
        every { getNotesPagedFlowUseCase.invoke() } returns flowOf(PagingData.empty())

        viewModel = NoteListViewModel(getNotesPagedFlowUseCase, deleteNoteUseCase)
    }

    @Test
    fun `deleteNote emits success effect when useCase succeeds`() = runTest {
        // Arrange
        coEvery { deleteNoteUseCase.invoke(1L) } returns Result.success(Unit)

        // Act
        viewModel.onEvent(NoteListEvent.DeleteNote(1L))

        // Assert
        val effect = viewModel.effect.first()
        assertEquals(NoteListEffect.ShowDeleteSuccessMessage, effect)
    }

    @Test
    fun `deleteNote emits failure effect when useCase fails`() = runTest {
        // Arrange
        coEvery { deleteNoteUseCase.invoke(1L) } returns Result.failure(Exception("delete failed"))

        // Act
        viewModel.onEvent(NoteListEvent.DeleteNote(1L))

        // Assert
        val effect = viewModel.effect.first()
        assertEquals(NoteListEffect.ShowDeleteFailedMessage, effect)
    }
}