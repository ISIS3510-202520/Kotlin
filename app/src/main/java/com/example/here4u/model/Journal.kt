package com.example.here4u.model

data class Journal(
    val id: Long,
    val date: Long,
    val content: String,
    val emotion: Emotion   // full object, with name + score
)

