package com.example.here4u.data.repositories

import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    fun getByDateRange(fromInclusive: Long, limit: Int= 100): Flow<List<JournalEntity>> {
        return journalDao.getSince(fromInclusive, limit)
    }

    fun getByEmotion(emotion: EmotionEntity): Flow<List<JournalEntity>> {
        return journalDao.getForEmotion(emotion.id)
    }
}
