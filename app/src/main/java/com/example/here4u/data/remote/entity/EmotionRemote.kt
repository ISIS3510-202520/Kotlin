package com.example.here4u.data.remote.entity

import com.google.firebase.firestore.DocumentId

data class EmotionRemote(
    @DocumentId val id: String? = null, // Firestore doesn't auto include ID in object
    val name: String = "",
    val description: String = "",
    val colorHex: String = ""
)