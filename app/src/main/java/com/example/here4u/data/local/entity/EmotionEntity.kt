package com.example.here4u.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Emotions_table")
data class EmotionEntity (@PrimaryKey(autoGenerate = true)
                          val  id: Long = 0,
                          val color: String,
                          val description: String,
                          val name: String )