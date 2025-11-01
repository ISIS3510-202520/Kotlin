package com.example.here4u.data.local.cache

import android.content.Context
import android.util.LruCache

object UserSessionCache {


    private val memoryCache = LruCache<String, String>(1)

    private const val PREFS_NAME = "user_cache"
    private const val KEY_UID = "user_uid"


    fun saveUser(context: Context, uid: String) {

        memoryCache.put(KEY_UID, uid)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_UID, uid).apply()
    }


    fun getUser(context: Context): String? {

        val cached = memoryCache.get(KEY_UID)
        if (cached != null) return cached


        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val uid = prefs.getString(KEY_UID, null)


        if (uid != null) memoryCache.put(KEY_UID, uid)

        return uid
    }


    fun clearUser(context: Context) {
        memoryCache.remove(KEY_UID)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}