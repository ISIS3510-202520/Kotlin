package com.example.here4u.data.repositories

import android.util.Log
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.model.Emotion
import com.example.here4u.model.Journal
import com.example.here4u.data.local.SeedEmotions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.here4u.model.JournalWithEmotion
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit


class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {

    suspend fun addTextJournal(emotionId: Long, content: String) {
        val entryEntity = JournalEntity(
            emotionId = emotionId,
            content = content,
            date = System.currentTimeMillis()
        )
        journalDao.insert(entryEntity)
    }

    suspend fun delete(entry: JournalEntity) {
        journalDao.delete(entry)
    }

    fun getByDateRange(from: Long): Flow<List<Journal>> {
        return journalDao.getSince(from).map { relations ->
            relations.map { it.toDomain() }
        }
    }

    fun getByEmotion(emotionId: Long): Flow<List<Journal>> {
        return journalDao.getForEmotion(emotionId).map { relations ->
            relations.map { it.toDomain() }
        }
    }

    fun getLast7Days(): Flow<List<Journal>> {
        val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
        return journalDao.getSince(sevenDaysAgo, limit = 100).map { relations ->
            Log.d("JournalRepository", "Mapping ${relations.size} relations")
            relations.map { it.toDomain() }
        }
    }

    fun getCurrentStreak(): Flow<Int> {
        return journalDao.getAllJournalDates().map { dates ->
            if (dates.isEmpty()) {
                Log.d("JournalRepository", "No journal entries found.")
                return@map 0
            }

            // Normalize all dates to day precision
            val dayTimestamps = dates.map { it.toDayOnly() }.distinct().sorted()
            Log.d("JournalRepository", "Raw dates: $dates")
            Log.d("JournalRepository", "Day-only dates (sorted): $dayTimestamps")

            var streak = 1

            for (i in dayTimestamps.size - 1 downTo 1) {
                val diffDays = dayTimestamps[i] - dayTimestamps[i - 1]
                Log.d("JournalRepository", "Comparing ${dayTimestamps[i]} and ${dayTimestamps[i - 1]} -> diffDays=$diffDays")

                if (diffDays == 1L) {
                    streak++
                } else {
                    Log.d("JournalRepository", "Streak broken at index $i")
                    break
                }
            }

            // Check if the last entry is today
            val today = System.currentTimeMillis().toDayOnly()
            Log.d("JournalRepository", "Today (dayOnly): $today, Last entry: ${dayTimestamps.last()}")

            if (dayTimestamps.last() != today) {
                Log.d("JournalRepository", "No entry for today, resetting streak to 0")
                //streak = 0
            }

            Log.d("JournalRepository", "Final streak = $streak")
            streak
        }
    }


    /**
     * Helper to normalize timestamp to midnight (day precision).
     */
    private fun Long.toDayOnly(): Long {
        return this / TimeUnit.DAYS.toMillis(1)
    }




    // --- Mapping function from JournalWithEmotion-> Domain ---
    private fun JournalWithEmotion.toDomain(): Journal {
        return Journal(
            id = journal.id,
            date = journal.date,
            content = journal.content,
            emotion = Emotion(
                id = emotion.id,
                name = emotion.name,
                color = emotion.color,
                description = emotion.description
                // score = emotion.score
            )
        )
    }
}



