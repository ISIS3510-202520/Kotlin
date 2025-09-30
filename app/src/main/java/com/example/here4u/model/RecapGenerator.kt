package com.example.here4u.model

object RecapGenerator {
    fun generate(journals: List<Journal>): Recap {
        // TODO: Replace with real stats + AI later
        return Recap(
            highlights = listOf("Most positive days: Week 2", "Stress peaks: Mondays"),
            summary = "Your emotional journey this month was a mix of highs and lows..."
        )
    }
}
