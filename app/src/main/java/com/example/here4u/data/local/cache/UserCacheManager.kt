package com.example.here4u.data.local.cache

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class CachedUser(
    val uid: String,
    val email: String?,
    val displayName: String?
)

@Singleton
class UserCacheManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("user_cache", Context.MODE_PRIVATE)

    fun saveUser(user: FirebaseUser) {
        prefs.edit()
            .putString("uid", user.uid)
            .putString("email", user.email)
            .putString("displayName", user.displayName)
            .apply()
    }

    fun getCachedUser(): CachedUser? {
        val uid = prefs.getString("uid", null) ?: return null
        val email = prefs.getString("email", null)
        val displayName = prefs.getString("displayName", null)
        return CachedUser(uid, email, displayName)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("uid")
    }
}
