package com.example.here4u.data.local.repositories

import android.util.Log
import com.example.here4u.data.local.cache.JournalMemoryCache
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalLocalRepository @Inject constructor(
    private val journalDao: JournalDao,
    private val journalRemoteRepository: JournalRemoteRepository,
    private val auth: FirebaseAuth,
    private val memoryCache: JournalMemoryCache


) {
    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insertJournal(journal)
        updateCache()
    }
    suspend fun updateCache() {
        val userId = auth.currentUser?.uid ?: return
        val lastFive = journalDao.getLastFive(userId)
        memoryCache.putAll(userId, lastFive)

    }

    suspend fun syncPendingJournals() {
        val pending = journalDao.getPending()
        if (pending.isEmpty()) return
        Log.d("holA", "🚀 Pendientes encontrados: ${pending.map { it.id }}")
        pending.forEach { j ->

            journalRemoteRepository.insertOne(j.emotionId, j.description,j.createdAt, j.userId)
            Log.d("SYNC", "🔹 Marcando journal id=${j.id} como sincronizado")
            journalDao.markAsSyncedById(j.id)
            Log.d("SYNC", "✅ Journal id=${j.id} actualizado a sync=true")

        }

        updateCache()
    }

    fun getCachedJournals(): List<JournalEntity> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        return memoryCache.getAll(userId)
    }
    fun clearUserCache() {
        val userId = auth.currentUser?.uid ?: return
        memoryCache.clearUser(userId)
    }



}
