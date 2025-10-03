package com.example.here4u.data.remote.entity

data class JournalRemote(
    val id: String = "",
    val emotionId: String = "",
    val userId: String = "",
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(), // Firestore string → use Long for consistency
    val sharedWithTherapist: Boolean = false
)