package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.model.JournalWithEmotion
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(journals: List<JournalEntity>)

    @Query("SELECT * FROM Emotions")
    fun getAll(): Flow<List<JournalEntity>>
    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM Journals WHERE id = :id")
    suspend fun deleteById(id: String)
    @Query("SELECT createdAt FROM Journals ORDER BY createdAt ASC")
    fun getAllJournalDates(): Flow<List<Long>>

    @Query("""
        SELECT * FROM Journals
        WHERE createdAt >= :fromInclusive
        ORDER BY createdAt DESC
    """)
    fun getSince(
        fromInclusive: Long,
    ): Flow<List<JournalWithEmotion>>



    @Query("""
        SELECT * FROM Journals
        WHERE emotionId = :emotionId
        ORDER BY createdAt DESC
    """)
    fun getForEmotion(emotionId: String): Flow<List<JournalEntity>>


}
