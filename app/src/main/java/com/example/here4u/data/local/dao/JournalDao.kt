package com.example.here4u.data.local.dao
import androidx.room.*
import com.example.here4u.data.local.entity.JournalEntity

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_table WHERE userId = :userId")
    suspend fun getJournalsByUserId(userId: String?): List<JournalEntity>
    @Insert
    suspend fun insertJournal(journal: JournalEntity)

    @Query("SELECT * FROM journal_table  WHERE sync = 0 ")
    suspend fun getPending(): List<JournalEntity>

    @Update
    suspend fun updateAll(items: List<JournalEntity>)

    @Query("UPDATE journal_table SET sync = 1 WHERE id = :journalId")
    suspend fun markAsSyncedById(journalId: Int)

    @Query("""
    SELECT * FROM journal_table 
    WHERE userId = :userId 
    ORDER BY createdAt DESC 
    LIMIT 5
""")
    suspend fun getLastFive(userId: String): List<JournalEntity>

}