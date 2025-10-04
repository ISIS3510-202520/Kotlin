package com.example.here4u.data.remote.openai

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Body request: ChatRequest
    ): ChatResponse
}
