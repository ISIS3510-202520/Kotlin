package com.example.here4u.data.local.entity

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Emotions_table")
data class EmotionEntity (@PrimaryKey(autoGenerate = true) val  id: Long = 0, @ColumnInfo val name: String, @ColorInt val color: Int, val description: String )