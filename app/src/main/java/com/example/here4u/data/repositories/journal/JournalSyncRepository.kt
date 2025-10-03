package com.example.here4u.data.repositories.journal

import android.R
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.remote.dto.EmotionDto
import com.example.here4u.data.remote.dto.JournalDto
import com.example.here4u.data.repositories.journal.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.contracts.contract

class JournalSyncRepository (private val localRepo: JournalRepository, private val remoteRepo: JournalRepositoryFirebase) {

    fun JournalEntity.toDto() = JournalDto(
        id = id, userId = userId, emotionId=emotionId,description = description, createdAt=createdAt,
        sharedWithTherapist = shareWithTherapist
    )

    suspend fun addJournal(emotionId: String, userId:String,content: String) {
        val id = localRepo.addTextJournal(emotionId,userId,content)
       remoteRepo.addJournal(id,emotionId,userId, content)
    }

    suspend fun delete(id:String,userId: String) {
        localRepo.deletebyid(id)
        remoteRepo.deleteJournal(id,userId)
    }

    fun getByDateRange(from: Long): Flow<List<JournalEntity>> {
        return localRepo.getByDateRange(from)
    }

    fun getByEmotion(emotion: EmotionEntity): Flow<List<JournalEntity>> {
        return localRepo.getByEmotion(emotion)
    }

    suspend fun syncUpSimple(userId: String): Result<Int> = runCatching {
        val lastRemoteCreatedAt = remoteRepo.fetchLastRemoteCreatedAt(userId)

        val toUpload = localRepo.getByDateRange(lastRemoteCreatedAt).first()
        if (toUpload.isEmpty()) return@runCatching 0

        remoteRepo.upsertAll(toUpload.map { it.toDto() })
        toUpload.size

    }




}