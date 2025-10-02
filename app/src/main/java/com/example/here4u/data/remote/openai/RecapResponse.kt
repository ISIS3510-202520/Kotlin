package com.example.here4u.data.remote.openai

data class RecapResponse(
    val highlights: List<String>,
    val summary: String
)