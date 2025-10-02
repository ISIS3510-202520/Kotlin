package com.example.here4u.data.remote.openai

data class ChatRequest(
    val model: String = "gpt-4o-mini",   // fast + cheap option
    val messages: List<ChatMessage>,
    val max_tokens: Int = 300
)
