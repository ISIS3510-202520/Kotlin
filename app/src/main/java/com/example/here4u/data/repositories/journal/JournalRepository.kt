package com.example.here4u.data.repositories.journal

import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {

    suspend fun addTextJournal(emotionId: String, userId:String,content: String):String {
        val entryEntity = JournalEntity(
            emotionId = emotionId,
            userId = userId,
            description = content,
            createdAt = System.currentTimeMillis(),
            shareWithTherapist = false
        )
        journalDao.insert(entryEntity)
        return entryEntity.id
    }

    suspend fun deletebyid(id: String) {
        journalDao.deleteById(id)
    }

    fun getByDateRange(fromInclusive: Long, limit: Int= 100): Flow<List<JournalEntity>> {
        return journalDao.getSince(fromInclusive)
    }

    fun getAll():Flow<List<EmotionEntity>>{
        return journalDao.getAll()
    }

    fun getByEmotion(emotion: EmotionEntity): Flow<List<JournalEntity>> {
        return journalDao.getForEmotion(emotion.id)
    }
}