package com.andy.data.repository

import com.andy.database.dao.NoteDao
import com.andy.database.entity.NoteEntity
import com.andy.model.Note
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryImplTest {

    private lateinit var dao: NoteDao
    private lateinit var repository: NoteRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = NoteRepositoryImpl(dao, testDispatcher)
    }

    @Test
    fun `create note delegates to dao`() = runTest(testDispatcher) {
        val note = Note(1L, "Title", "Content", updatedAt = 1L)
        coEvery { dao.insert(any()) } returns 1L

        val result = repository.create(note)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `update note delegates to dao`() = runTest(testDispatcher) {
        coEvery { dao.update(any()) } returns Unit

        val result = repository.update(1L, "New title", "New content")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `delete note delegates to dao`() = runTest(testDispatcher) {
        coEvery { dao.deleteById(1L) } returns Unit

        val result = repository.delete(1L)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `getNote maps entity to model`() = runTest(testDispatcher) {
        val entity = NoteEntity(1L, "Title", "Content", updatedAt = 1L)
        every { dao.getNoteById(1L) } returns flowOf(entity)

        val note = repository.getNote(1L).first()

        assertNotNull(note)
        assertEquals("Title", note?.title)
        assertEquals("Content", note?.content)
    }
}