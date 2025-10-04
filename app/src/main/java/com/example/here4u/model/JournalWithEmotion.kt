package com.example.here4u.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity

data class JournalWithEmotion(
    @Embedded val journal: JournalEntity,
    @Relation(
        parentColumn = "emotionId",
        entityColumn = "id"
    )
    val emotion: EmotionEntity
)
