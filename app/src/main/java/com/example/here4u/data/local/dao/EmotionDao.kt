package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.here4u.data.local.entity.EmotionEntity
import androidx.room.Room
@Dao
interface EmotionDao {


    @Query("SELECT * FROM Emotions_table WHERE id = :emotionId")
    suspend fun getEmotionById(emotionId: String): EmotionEntity?

    @Query("SELECT * FROM Emotions_table")
    suspend fun getAll(): List<EmotionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(emotions: List<EmotionEntity>)

    @Query("DELETE FROM Emotions_table")
    suspend fun clearAll()
    @Insert
    suspend fun insertEmotion(emotion: EmotionEntity)






}