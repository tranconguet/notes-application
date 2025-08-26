package com.andy.notesapplication.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.andy.data.repository.NoteRepository
import com.andy.domain.CreateNoteUseCase
import com.andy.domain.DeleteNoteUseCase
import com.andy.domain.GetNoteFlowUseCase
import com.andy.domain.UpdateNoteUseCase
import com.andy.model.Note
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailViewModelFactory(
    private val noteId: Long?,
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(
            noteId = noteId,
            getNoteFlowUseCase = GetNoteFlowUseCase(repository),
            createNoteUseCase = CreateNoteUseCase(repository),
            updateNoteUseCase = UpdateNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository)
        ) as T
    }
}

class NoteDetailViewModel(
    private val noteId: Long?,
    private val getNoteFlowUseCase: GetNoteFlowUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    val uiState = noteId?.let { noteId ->
        getNoteFlowUseCase(noteId)
            .map {
                if (it != null) {
                    NoteDetailUiState.Success(it)
                } else {
                    NoteDetailUiState.Error
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NoteDetailUiState.Loading
            )
    } ?: MutableStateFlow(NoteDetailUiState.New)

    private val _effect = Channel<NoteDetailEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: NoteDetailEvent) {
        when (event) {

            is NoteDetailEvent.UpdateNote -> {
                updateNote(event.newTitle, event.newContent)
            }

            is NoteDetailEvent.DeleteNote -> {
                deleteNote()
            }

            is NoteDetailEvent.AddNote -> {
                createNote(event.title, event.content)
            }
        }
    }

    private fun sendEffect(effect: NoteDetailEffect) {
        _effect.trySend(effect)
    }

    private fun updateNote(newTitle: String, newContent: String) {
        viewModelScope.launch {
            noteId?.let {
                val result = updateNoteUseCase(
                    id = it,
                    title = newTitle,
                    content = newContent
                )
                sendEffect(
                    if (result.isSuccess) {
                        NoteDetailEffect.NavigateBack
                    } else {
                        NoteDetailEffect.ShowSaveFailedMessage
                    }
                )
            }
        }
    }

    private fun createNote(newTitle: String, newContent: String) {
        viewModelScope.launch {
            val result = createNoteUseCase(
                Note(
                    title = newTitle,
                    content = newContent,
                    updatedAt = System.currentTimeMillis()
                )
            )
            sendEffect(
                if (result.isSuccess) {
                    NoteDetailEffect.NavigateBack
                } else {
                    NoteDetailEffect.ShowSaveFailedMessage
                }
            )
        }
    }

    private fun deleteNote() {
        noteId?.let {
            viewModelScope.launch {
                val result = deleteNoteUseCase(id = noteId)
                sendEffect(
                    if (result.isSuccess) {
                        NoteDetailEffect.NavigateBack
                    } else {
                        NoteDetailEffect.ShowDeleteFailedMessage
                    }
                )
            }
        }
    }

}