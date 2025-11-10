package com.example.here4u.data.local.preferences

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val LAST_SAVE_KEY = "last_save_time"

    private val auth = FirebaseAuth.getInstance()


    fun saveLastSaveTime(timestamp: Long) {
        val userId = auth.currentUser?.uid ?: return
        prefs.edit().putLong("${userId}_$LAST_SAVE_KEY", timestamp).apply()
    }


    fun getLastSaveTime(): Long {
        val userId = auth.currentUser?.uid ?: return 0L
        return prefs.getLong("${userId}_$LAST_SAVE_KEY", 0L)
    }


    fun clearUserPreferences() {
        val userId = auth.currentUser?.uid ?: return
        prefs.edit().remove("${userId}_$LAST_SAVE_KEY").apply()
    }
}
