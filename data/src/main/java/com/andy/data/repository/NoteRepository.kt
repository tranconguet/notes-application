package com.andy.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andy.data.mapper.toEntity
import com.andy.data.mapper.toModel
import com.andy.database.dao.NoteDao
import com.andy.database.entity.NoteEntity
import com.andy.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface NoteRepository {
    fun getNotesPagedFlow(): Flow<PagingData<Note>>
    fun getNote(id: Long): Flow<Note?>
    suspend fun update(id: Long, title: String, content: String): Result<Unit>
    suspend fun create(note: Note): Result<Unit>
    suspend fun delete(id: Long): Result<Unit>
}

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun getNotesPagedFlow(): Flow<PagingData<Note>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getNotesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
    }

    override fun getNote(id: Long): Flow<Note?> = dao.getNoteById(id).map { it?.toModel() }

    override suspend fun create(note: Note): Result<Unit> = runCatching {
        withContext(dispatcher) {
            dao.insert(note.toEntity())
        }
    }

    override suspend fun update(id: Long, title: String, content: String): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                dao.update(
                    NoteEntity(
                        id = id,
                        title = title.trim(),
                        content = content.trim(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }

    override suspend fun delete(id: Long): Result<Unit> = runCatching {
        withContext(dispatcher) {
            dao.deleteById(id)
        }
    }

}