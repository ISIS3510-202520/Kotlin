package com.example.here4u.data.local.dao
import androidx.room.*
import com.example.here4u.data.local.entity.JournalEntity

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_table WHERE userId = :userId")
    suspend fun getJournalsByUserId(userId: String?): List<JournalEntity>
    @Insert
    suspend fun insertJournal(journal: JournalEntity)

}