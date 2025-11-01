package com.example.here4u.data.local.repositories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyRequestLocalRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("Here4UPrefs", Context.MODE_PRIVATE)
    private val LAST_EMERGENCY_DATE = "last_emergency_date"



    fun saveLastEmergencyDate(date: String) {
        prefs.edit().putString(LAST_EMERGENCY_DATE, date).apply()
    }

    fun getLastEmergencyDate(): String? {
        return prefs.getString(LAST_EMERGENCY_DATE, null)
    }


}