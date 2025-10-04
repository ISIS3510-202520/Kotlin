package com.example.here4u.data.mappers

import android.graphics.Color
import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.entity.JournalRemote
import com.example.here4u.model.Emotion
import com.example.here4u.model.Journal

object JournalMapper {

    fun toDomain(remote: JournalRemote, emotion: EmotionRemote?): Journal {
        val emotionModel = emotion?.let {
            Emotion(
                id = it.id.hashCode().toLong(), // Firestore id → Long
                name = it.name,
                color = try {
                    Color.parseColor(it.colorHex)
                } catch (e: Exception) {
                    Color.GRAY
                },
                description = it.description
            )
        } ?: Emotion(
            id = -1L,
            name = "Unknown",
            color = Color.GRAY,
            description = "Emotion data not found"
        )

        return Journal(
            id = remote.id,  // Firestore id → Long
            date = remote.createdAt,
            content = remote.description,
            emotion = emotionModel
        )
    }
}
