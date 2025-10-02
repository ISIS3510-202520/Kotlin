package com.example.here4u.model

import androidx.annotation.ColorInt

data class Emotion(
    val id: Long,
    val name: String,
    val color: Int,
    val description: String,
    val score: Float
)