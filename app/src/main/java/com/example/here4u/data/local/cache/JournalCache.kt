package com.example.here4u.data.local.cache

import android.util.LruCache
import com.example.here4u.data.local.entity.JournalEntity
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalMemoryCache @Inject constructor(
    private val auth:FirebaseAuth,
) {

    private val userCaches = mutableMapOf<String, LruCache<Int, JournalEntity>>()

    fun putAll(userId: String, journals: List<JournalEntity>) {
        val cache = userCaches.getOrPut(userId) {
            object : LruCache<Int, JournalEntity>(5) {
                override fun sizeOf(key: Int, value: JournalEntity) = 1
            }
        }
        journals.take(5).forEach { cache.put(it.id, it) }
    }

    fun getAll(userId: String): List<JournalEntity> {
        return userCaches[userId]?.snapshot()?.values?.toList()
            ?.sortedByDescending { it.createdAt.seconds } ?: emptyList()
    }

    fun clearUser(userId: String) {
        userCaches.remove(userId)
    }

    fun clearAll() {
        userCaches.clear()
    }
}
