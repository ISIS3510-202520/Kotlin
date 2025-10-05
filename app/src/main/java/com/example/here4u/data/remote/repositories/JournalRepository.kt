package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.service.FirebaseService
import com.example.here4u.data.mappers.JournalMapper
import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.domain.model.Journal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val remoteRepo: JournalRemoteRepository,
    private val service: FirebaseService
) {

    fun getLast7Days(userId: String): Flow<List<Journal>> = flow {
        val journalRemotes = remoteRepo.getLast7Days(userId)
        val emotionCache = mutableMapOf<String, EmotionRemote>()

        val journals = journalRemotes.map { journalRemote ->
            val emotionId = journalRemote.emotionId

            // Lazy-load emotion from Firestore if not cached
            val emotion = emotionCache.getOrPut(emotionId) {
                service.emotions.document(emotionId)
                    .get()
                    .await()
                    .toObject(EmotionRemote::class.java)
                    ?: EmotionRemote()
            }

            JournalMapper.toDomain(journalRemote, emotion)
        }

        emit(journals)
    }
}
