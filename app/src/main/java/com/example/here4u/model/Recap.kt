package com.example.here4u.model


data class Recap(
    val highlights: List<String>,
    val summary: String,
    val trendPoints: List<TrendPoint>   // NEW
)

data class TrendPoint(
    val date: Long,    // epoch millis
    val score: Float   // emotion score
)