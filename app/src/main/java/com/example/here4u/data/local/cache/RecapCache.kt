package com.example.here4u.data.local.cache

import com.example.here4u.domain.model.Recap
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecapCache @Inject constructor() {

    private val cache = ConcurrentHashMap<String, Recap>()

    fun get(key: String): Recap? = cache[key]

    fun put(key: String, recap: Recap) {
        cache[key] = recap
    }

    fun clear() {
        cache.clear()
    }
}