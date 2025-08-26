package com.andy.notesapplication

import android.content.Context
import com.andy.data.repository.NoteRepository
import com.andy.data.repository.NoteRepositoryImpl
import com.andy.database.AppDatabase

object ServiceLocator {

    @Volatile
    private var database: AppDatabase? = null
    @Volatile
    private var repository: NoteRepository? = null

    fun provideNoteRepository(context: Context): NoteRepository {
        return repository ?: synchronized(this) {
            repository ?: NoteRepositoryImpl(provideDatabase(context).noteDao()).also {
                repository = it
            }
        }
    }

    private fun provideDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: AppDatabase.build(context).also {
                database = it
            }
        }
    }
}