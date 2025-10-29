package com.example.here4u.data.local.security
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePrefs {

    fun create(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(context: Context, key: String, value: String?) {
        val prefs = create(context)
        prefs.edit().putString(key, value).apply()
    }

    fun read(context: Context, key: String): String? {
        val prefs = create(context)
        return prefs.getString(key, null)
    }

    fun clear(context: Context) {
        val prefs = create(context)
        prefs.edit().clear().apply()
    }
}
