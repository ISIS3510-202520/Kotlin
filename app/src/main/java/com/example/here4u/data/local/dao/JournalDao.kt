package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity)

    @Query("SELECT * FROM Emotions")
    fun getAll(): Flow<List<EmotionEntity>>
    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM Journals WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("""
        SELECT * FROM Journals
        WHERE createdAt >= :fromInclusive
        ORDER BY createdAt DESC
    """)
    fun getSince(
        fromInclusive: Long,
    ): Flow<List<JournalEntity>>

    @Query("""
        SELECT * FROM Journals
        WHERE emotionId = :emotionId
        ORDER BY createdAt DESC
    """)
    fun getForEmotion(emotionId: String): Flow<List<JournalEntity>>


}
