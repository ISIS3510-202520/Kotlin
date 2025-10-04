package com.example.here4u.model

import com.google.firebase.Timestamp

data class Journal(
    val id: String,
    val date: Long,
    val content: String,
    val emotion: Emotion   // full object, with name + score
)

