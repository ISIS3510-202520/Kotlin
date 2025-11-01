package com.example.here4u.data.local.cache
import android.util.LruCache

object SessionCache {
    private val cache= LruCache<String,String>(1)

    fun saveToken(token: String) {
        cache.put("token", token)
    }



    fun getUser(): String? = cache.get("user_uid")

    fun clear()=cache.evictAll()
}