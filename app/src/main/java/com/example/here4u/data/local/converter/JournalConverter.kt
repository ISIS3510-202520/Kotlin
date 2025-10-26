package com.example.here4u.data.local.converter

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

class JournalConverter {

    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long? {
        return value?.seconds
    }

    @TypeConverter
    fun toTimestamp(seconds: Long?): Timestamp? {
        return seconds?.let { Timestamp(it, 0) }
    }
}
