package com.example.here4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_table")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String?,
    val emotionId: String,
    val description: String,
    val createdAt: com.google.firebase.Timestamp,
    val sync: Boolean =false
)
