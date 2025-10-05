package com.example.here4u.data.remote.entity

data class SummaryRequestRemote(
    val id: String = "",
    val userId: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val generatedAt: Long = System.currentTimeMillis(),
    val summaryText: String = ""
)
