package com.example.here4u.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- CAMBIOS PRINCIPALES AQUÍ ---

// 1. Se añade la anotación @HiltViewModel para que Hilt sepa que debe gestionar este ViewModel.
@HiltViewModel
// 2. Se añade la anotación @Inject en el constructor para que Hilt inyecte las dependencias necesarias.
class ProfileViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult

    fun logout() {
        viewModelScope.launch {
            try {
                userRemoteRepository.logout()
                _logoutResult.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _logoutResult.postValue(false)
            }
        }
    }
}
