package com.example.here4u.data.local.dao
import androidx.room.*
import com.example.here4u.data.local.entity.EmotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {

    @Query("SELECT * FROM Emotions")
    fun getAll(): Flow<List<EmotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(emotions: List<EmotionEntity>)

    @Query("DELETE FROM Emotions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Insert
    suspend fun insertOne(item: EmotionEntity)

    @Upsert
    suspend fun upsertOne(item: EmotionEntity)

    @Query("SELECT * FROM Emotions WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): EmotionEntity?
}