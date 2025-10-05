package com.example.here4u.data.remote.openai


data class ChatResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: ChatMessage
    )
}