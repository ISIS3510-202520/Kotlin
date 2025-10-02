package com.example.here4u.data.repositories

import com.example.here4u.model.Recap
import com.example.here4u.data.remote.openai.ChatMessage
import com.example.here4u.data.remote.openai.ChatRequest
import com.example.here4u.data.remote.openai.OpenAIApi
import com.example.here4u.model.Journal
import com.example.here4u.model.TrendPoint
import com.google.gson.Gson
import com.example.here4u.data.remote.openai.RecapResponse
import org.json.JSONObject
import javax.inject.Inject

class RecapRepository @Inject constructor(
    private val openAiApi: OpenAIApi
) {
    suspend fun generateRecapWithAI(journals: List<Journal>): Recap {
        // 1. Build prompt with JSON instructions
        val prompt = buildPromptFromJournals(journals)

        val request = ChatRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                ChatMessage(
                    "system",
                    """
                    You are a helpful assistant.
                    Respond in JSON with the following structure:
                    {
                      "highlights": ["...", "...", "..."],   // exactly 3 concise bullet points
                      "summary": "short 3–5 sentence paragraph"
                    }
                    """.trimIndent()
                ),
                ChatMessage("user", prompt)
            )
        )

        // 2. Call OpenAI API
        val response = openAiApi.getChatCompletion(request)

        // 3. Parse AI response
        val rawContent = response.choices.firstOrNull()?.message?.content ?: "{}"

        val recapResponse: RecapResponse = try {
            Gson().fromJson(rawContent, RecapResponse::class.java)
        } catch (e: Exception) {
            // fallback if parsing fails
            RecapResponse(
                highlights = emptyList(),
                summary = rawContent.ifBlank { "No summary generated." }
            )
        }

        // 4. Build trend points from local journals
        val trendPoints = journals.map {
            TrendPoint(date = it.date, score = it.emotion.score)
        }

        val recap = Recap(
            highlights = recapResponse.highlights,
            summary = recapResponse.summary,
            trendPoints = trendPoints
        )
        // 5. Return Recap object ✅
        return Recap(
            highlights = recapResponse.highlights,
            summary = recapResponse.summary,
            trendPoints = trendPoints
        )

    }
    private fun buildPromptFromJournals(journals: List<Journal>): String {
        return "Here are the journals for this week:\n\n" +
                journals.joinToString("\n") { j ->
                    "Date: ${j.date}, Emotion: ${j.emotion.name}, Score: ${j.emotion.score}, Note: ${j.content}"
                }
    }
}
