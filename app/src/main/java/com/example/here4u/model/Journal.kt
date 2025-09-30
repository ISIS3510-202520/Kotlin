package com.example.here4u.model
import java.time.LocalDate  // Import LocalDate for date representation

data class Journal(
    val id: Int = 0,                // optional, if saving in DB
    val date: LocalDate,
    val emotion: Emotion,
    val content: String
)