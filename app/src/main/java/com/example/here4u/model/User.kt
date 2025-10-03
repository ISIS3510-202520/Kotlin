package com.example.here4u.model

data class User(
    val id: String,
    val displayName: String,
    val email: String,
    val createdAt: Long,
    val lastLogin: Long,
    val lastEntryDate: Long,
    val currentStreak: Int,
    val longestStreak: Int
)