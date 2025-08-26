package com.andy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andy.database.dao.NoteDao
import com.andy.database.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    companion object {
        fun build(context: Context): AppDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "notes.db"
            ).fallbackToDestructiveMigration(false).build()
    }
}