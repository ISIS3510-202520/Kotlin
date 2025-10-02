package com.example.here4u.data.local.dao
import androidx.room.*
import com.example.here4u.data.local.entity.EmotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {

    @Query("SELECT * FROM Emotions_table")
    fun getAll(): Flow<List<EmotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(emotions: List<EmotionEntity>)
}