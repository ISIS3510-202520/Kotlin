package com.example.here4u.data.local.converter

import androidx.room.TypeConverter

class EmotionsConverter {

    @TypeConverter
    fun fromEmotionsList(emotions: List<String>): String {
        // Join the list of strings into a single comma-separated string.
        return emotions.joinToString(",")
    }

    @TypeConverter
    fun toEmotionsList(data: String): List<String> {
        // Split the string by commas to get the list back.
        // Returns an empty list if the data string is empty.
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.split(",")
        }
    }
}