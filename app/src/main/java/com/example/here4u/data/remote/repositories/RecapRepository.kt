package com.example.here4u.data.remote.repositories

import com.example.here4u.domain.model.Recap
import com.example.here4u.data.remote.openai.ChatMessage
import com.example.here4u.data.remote.openai.ChatRequest
import com.example.here4u.data.remote.openai.OpenAIApi
import com.example.here4u.data.remote.openai.RecapResponse
import com.example.here4u.data.remote.entity.SummaryRequestRemote
import com.example.here4u.domain.model.Journal
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RecapRepository @Inject constructor(
    private val openAiApi: OpenAIApi,
    private val summaryRequestRepo: SummaryRequestRemoteRepository
) {
    suspend fun generateRecapWithAI(userId: String, journals: List<Journal>): Recap {


        val mostCommonEmotion = journals
            .groupingBy { it.emotion.name }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key ?: "Unknown"


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


        val response = openAiApi.getChatCompletion(request)


        val rawContent = response.choices.firstOrNull()?.message?.content ?: "{}"

        val recapResponse: RecapResponse = try {
            Gson().fromJson(rawContent, RecapResponse::class.java)
        } catch (e: Exception) {
            RecapResponse(
                highlights = emptyList(),
                summary = rawContent.ifBlank { "No summary generated." }
            )
        }


        val modifiedSummary = "Most common emotion: $mostCommonEmotion\n\n${recapResponse.summary}"

        val recap = Recap(
            highlights = recapResponse.highlights,
            summary = modifiedSummary
        )


        saveSummaryRequest(userId, recap.summary)

        return recap
    }

    private fun buildPromptFromJournals(journals: List<Journal>): String {
        return "Here are the journals for this week:\n\n" +
                journals.joinToString("\n") { j ->
                    "Date: ${j.date}, Emotion: ${j.emotion.name}, Note: ${j.content}"
                }
    }

    private suspend fun saveSummaryRequest(userId: String, summaryText: String) {
        val now = System.currentTimeMillis()
        val sevenDaysAgo = now - TimeUnit.DAYS.toMillis(6)

        val summaryRequest = SummaryRequestRemote(
            userId = userId,
            startDate = sevenDaysAgo,
            endDate = now,
            summaryText = summaryText
        )

        summaryRequestRepo.insertOne(summaryRequest)
        }
}