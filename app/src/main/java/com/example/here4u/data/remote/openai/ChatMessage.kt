package com.example.here4u.data.remote.openai

// ChatMessage.kt
data class ChatMessage(
    val role: String,   // "system", "user", or "assistant"
    val content: String
)