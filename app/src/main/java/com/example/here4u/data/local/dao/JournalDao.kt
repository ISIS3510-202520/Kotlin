package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity): Long

    @Update
    suspend fun update(entry: JournalEntity)

    @Delete
    suspend fun delete(entry: JournalEntity)

    @Query("DELETE FROM Journal_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT * FROM Journal_table
        WHERE date >= :fromInclusive
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun getSince(
        fromInclusive: Long,
        limit: Int = 100
    ): Flow<List<JournalEntity>>

    @Query("""
        SELECT * FROM Journal_table
        WHERE emotionId = :emotionId
        ORDER BY date DESC
    """)
    fun getForEmotion(emotionId: Long): Flow<List<JournalEntity>>
}
