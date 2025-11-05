package com.example.here4u.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import com.example.here4u.data.local.cache.JournalMemoryCache
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository,
    private val journalcache: JournalLocalRepository,

    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult


    private val prefs = context.getSharedPreferences("user_cache", Context.MODE_PRIVATE)

    fun logout() {
        viewModelScope.launch {
            try {
                journalcache.clearUserCache()
                FirebaseAuth.getInstance().signOut()
                prefs.edit().remove("uid").apply()
                userRemoteRepository.logout()
                _logoutResult.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _logoutResult.postValue(false)
            }
        }
    }
}