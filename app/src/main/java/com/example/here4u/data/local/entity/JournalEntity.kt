package com.example.here4u.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Journal_table",
        foreignKeys = [
            ForeignKey(
                entity = EmotionEntity::class,
                parentColumns = ["id"],
                childColumns = ["emotionId"],
                onDelete = ForeignKey.CASCADE)],
        indices = [Index("emotionId")])

class JournalEntity(@PrimaryKey(autoGenerate = true) val  id: Long = 0,
    val emotionId: Long,
    val content: String, val date: Long
    ) {
}