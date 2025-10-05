package com.example.here4u.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

class JournalRemoteDocuments (
    @DocumentId val id: String = "",
    val emotionId: String? = "",
    val userId: String? = "",
    val description: String = "",
     @ServerTimestamp val createdAt: Timestamp? = null,
    val sharedWithTherapist: Boolean = false)
