package com.example.here4u.data.local

import com.example.here4u.data.local.entity.JournalEntity

object SeedJournals {
    private const val ONE_DAY_MS = 1000L * 60 * 60 * 24

    val list = listOf(
        JournalEntity(
            emotionId = 1, // Serenity
            content = "Felt calm and relaxed during meditation today.",
            date = System.currentTimeMillis() - 6 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 2, // Joy
            content = "Had a great lunch with friends, laughed a lot.",
            date = System.currentTimeMillis() - 5 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 14, // Sadness
            content = "Missed a family call, feeling a bit down.",
            date = System.currentTimeMillis() - 4 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 25, // Anger
            content = "Got frustrated at work with a deadline.",
            date = System.currentTimeMillis() - 3 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 19, // Fear
            content = "Nervous about the upcoming presentation.",
            date = System.currentTimeMillis() - 2 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 28, // Surprise
            content = "Unexpectedly received a nice compliment.",
            date = System.currentTimeMillis() - 1 * ONE_DAY_MS
        ),
        JournalEntity(
            emotionId = 8, // Optimism
            content = "Feeling hopeful about starting a new project.",
            date = System.currentTimeMillis()
        )
    )
}
