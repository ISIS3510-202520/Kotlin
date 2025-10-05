package com.example.here4u.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class JournalRemote(
    val id: String = "",
    val emotionId: String = "",
    val userId: String = "",
    val description: String = "",
    val createdAt: Long = 0,
    val sharedWithTherapist: Boolean = false
)