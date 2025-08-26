package com.andy.model

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val updatedAt: Long
)