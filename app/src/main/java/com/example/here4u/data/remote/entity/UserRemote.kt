package com.example.here4u.data.remote.entity

data class UserRemote(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = System.currentTimeMillis(),
    val lastEntryDate: Long? = null,   // 👈 ✅ Ya no se actualiza sola
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)
