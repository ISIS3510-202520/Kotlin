package com.example.here4u.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.here4u.data.local.converter.EmotionsConverter
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.entity.EmotionEntity

@Database(entities = [EmotionEntity::class], version = 1)
@TypeConverters(EmotionsConverter::class)
abstract class EmotionDatabase: RoomDatabase(){
    abstract fun emotionDao(): EmotionDao


}