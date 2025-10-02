package com.example.here4u.model

object RecapGenerator {
    fun generate(journals: List<Journal>): Recap {
        val trendPoints = journals.map {
            TrendPoint(date = it.date, score = it.emotion.score)
        }

        return Recap(
            highlights = listOf("Most positive day: ...", "Stress peak: ..."),
            summary = "Your emotional journey this week shows ups and downs.",
            trendPoints = trendPoints
        )
    }
}
