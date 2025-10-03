package com.example.here4u.model

data class SummaryRequest(
    val id: String,
    val userId: String,
    val startDate: Long,
    val endDate: Long,
    val generatedAt: Long,
    val summaryText: String
)