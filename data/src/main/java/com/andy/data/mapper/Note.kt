package com.andy.data.mapper

import com.andy.database.entity.NoteEntity
import com.andy.model.Note

internal fun NoteEntity.toModel(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        updatedAt = updatedAt
    )
}

internal fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        updatedAt = updatedAt
    )
}