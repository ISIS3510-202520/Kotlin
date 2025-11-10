package com.example.here4u.data.local.cache

import android.util.Log
import android.util.LruCache
import com.example.here4u.data.local.entity.JournalEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class JournalMemoryCache @Inject constructor(
    private val auth:FirebaseAuth,
) {

    private val userCaches = LruCache<Int, JournalEntity>(5)
    private val _cacheFlow = MutableStateFlow<List<JournalEntity>>(emptyList())
    val cacheFlow: StateFlow<List<JournalEntity>> = _cacheFlow
    private var nextIndex = 0
    fun putAll(journals: List<JournalEntity>) {
        userCaches.evictAll()
        nextIndex = 0
        journals.forEach {
            userCaches.put(nextIndex++, it)
            if (nextIndex >= 5) nextIndex = 0
        }
        _cacheFlow.value= getAll()
    }

    fun getAll(): List<JournalEntity> {
        return userCaches.snapshot()?.values?.toList()
            ?.sortedByDescending { it.createdAt.seconds } ?: emptyList()

    }

    fun clearUser() {
        userCaches.evictAll()
        _cacheFlow.value = emptyList()
    }

    fun putOne(journal: JournalEntity) {
        userCaches.put(nextIndex, journal)
        Log.d("LO", "entrada \${userCache.get(nextIndex)} ")
        nextIndex = (nextIndex + 1) % 5
        Log.d("LO", "cache \${userCaches.size()} ")
        _cacheFlow.value = getAll()
    }

}
