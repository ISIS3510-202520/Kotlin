package com.example.here4u.data.local.entity

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Emotions")
data class EmotionEntity (@PrimaryKey val id: String = UUID.randomUUID().toString(), @ColumnInfo val name: String, @ColumnInfo val colorHex: String, @ColumnInfo val description: String )