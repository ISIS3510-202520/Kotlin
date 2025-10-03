package com.example.here4u.data.local.entity

import android.R
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Journals",
        foreignKeys = [
            ForeignKey(
                entity = EmotionEntity::class,
                parentColumns = ["id"],
                childColumns = ["emotionId"],
                onDelete = ForeignKey.CASCADE)],
        indices = [Index("emotionId")])

class JournalEntity(@PrimaryKey val id: String = UUID.randomUUID().toString(), @ColumnInfo val userId: String,
                    @ColumnInfo val emotionId: String, @ColumnInfo val description: String,
                    @ColumnInfo val createdAt: Long, @ColumnInfo val shareWithTherapist: Boolean) {
}