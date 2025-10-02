package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow
import com.example.here4u.model.JournalWithEmotion
import androidx.room.Transaction


@Dao
interface JournalDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<JournalEntity>)

    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM Journal_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    // ✅ Updated to return JournalWithEmotion
    @Transaction
    @Query("""
        SELECT * FROM Journal_table
        WHERE date >= :fromInclusive
        ORDER BY date ASC
        LIMIT :limit
    """)
    fun getSince(
        fromInclusive: Long,
        limit: Long = 100
    ): Flow<List<JournalWithEmotion>>

    @Transaction
    @Query("""
        SELECT * FROM Journal_table
        WHERE emotionId = :emotionId
        ORDER BY date DESC
    """)
    fun getForEmotion(emotionId: Long): Flow<List<JournalWithEmotion>>
}
