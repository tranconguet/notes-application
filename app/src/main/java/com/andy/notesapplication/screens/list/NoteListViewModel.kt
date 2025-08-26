package com.andy.notesapplication.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andy.data.repository.NoteRepository
import com.andy.domain.DeleteNoteUseCase
import com.andy.domain.GetNotesPagedFlowUseCase
import com.andy.model.Note
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteListViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteListViewModel(
            getNotesPagedFlowUseCase = GetNotesPagedFlowUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository)
        ) as T
    }
}

class NoteListViewModel(
    getNotesPagedFlowUseCase: GetNotesPagedFlowUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {

    val pagedNotes: StateFlow<PagingData<Note>> =
        getNotesPagedFlowUseCase()
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )

    private val _effect = Channel<NoteListEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: NoteListEvent) {
        when(event) {
            is NoteListEvent.DeleteNote -> deleteNote(event.id)
        }
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            val result = deleteNoteUseCase(id)
            sendEffect(
                if (result.isSuccess) {
                    NoteListEffect.ShowDeleteSuccessMessage
                } else {
                    NoteListEffect.ShowDeleteFailedMessage
                }
            )
        }
    }

    private fun sendEffect(effect: NoteListEffect) {
        _effect.trySend(effect)
    }
}