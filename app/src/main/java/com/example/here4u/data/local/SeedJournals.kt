package com.example.here4u.data.local

import com.example.here4u.data.local.entity.JournalEntity

object SeedJournals {
    private const val ONE_DAY_MS = 1000L * 60 * 60 * 24

    val list = listOf(
        JournalEntity(
            userId = "AAAA",
            emotionId = "1", // Serenity
            description = "Felt calm and relaxed during meditation today.",
            shareWithTherapist = false,
            createdAt = System.currentTimeMillis() - 6 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            emotionId = "2", // Joy
            shareWithTherapist = false,
            description = "Had a great lunch with friends, laughed a lot.",
            createdAt = System.currentTimeMillis() - 5 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            emotionId = "14", // Sadness
            description = "Missed a family call, feeling a bit down.",
            shareWithTherapist = false,
            createdAt = System.currentTimeMillis() - 4 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            shareWithTherapist = false,
            emotionId = "25", // Anger
            description = "Got frustrated at work with a deadline.",
            createdAt = System.currentTimeMillis() - 3 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            shareWithTherapist = false,
            emotionId = "19", // Fear
            description = "Nervous about the upcoming presentation.",
            createdAt = System.currentTimeMillis() - 2 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            shareWithTherapist = false,
            emotionId = "28", // Surprise
            description = "Unexpectedly received a nice compliment.",
            createdAt = System.currentTimeMillis() - 1 * ONE_DAY_MS
        ),
        JournalEntity(
            userId = "AAAA",
            shareWithTherapist = false,
            emotionId = "8", // Optimism
            description = "Feeling hopeful about starting a new project.",
            createdAt = System.currentTimeMillis()
        )
    )
}
