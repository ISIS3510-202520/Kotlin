package com.example.here4u.model

data class Journal(
    val id: String,
    val emotion: Emotion,
    val userId: String,
    val description: String,
    val createdAt: Long,
    val sharedWithTherapist: Boolean
)