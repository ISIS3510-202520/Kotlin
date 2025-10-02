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

class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {

    suspend fun insert(entry: JournalEntity) {
        journalDao.insert(entry)
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
                description = emotion.description,
                score = emotion.score
            )
        )
    }
}
