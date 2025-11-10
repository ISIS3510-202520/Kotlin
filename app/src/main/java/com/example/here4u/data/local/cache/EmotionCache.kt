package com.example.here4u.data.local.cache

import android.util.LruCache
import com.example.here4u.data.remote.entity.EmotionRemote

object EmotionCache {

    private val memoryCache = LruCache<String, List<EmotionRemote>>(1)
    private const val KEY = "emotion_list"

    fun save(emotions: List<EmotionRemote>) {
        memoryCache.put(KEY, emotions)
    }

    fun get(): List<EmotionRemote>? {
        return memoryCache.get(KEY)
    }

    fun clear() {
        memoryCache.remove(KEY)
    }
}
