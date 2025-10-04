package com.example.here4u.viewmodel

import android.util.Log

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.BuildConfig
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import com.example.here4u.data.remote.repositories.EmergencyRequestRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.model.LocationModelImpl
import com.example.here4u.view.emergency.Emergency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyContactsViewModel @Inject constructor(
    private val repository: EmergencyContactRemoteRepository,
    private val userRepository: UserRemoteRepository,
    private val  emergencyRepository: EmergencyRequestRemoteRepository,
    private val locationModelImpl: LocationModelImpl
) : ViewModel() {

    private val userId: String? = userRepository.getUserId()
    private val _createdId = MutableStateFlow<String?>(null)
    val createdId: StateFlow<String?> = _createdId

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    val contacts: StateFlow<List<EmergencyContactRemote>> =
        if (userId != null) {
            repository.getAll()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
        } else {
            flowOf(emptyList<EmergencyContactRemote>())
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
        }

    // 🔹 Enviar correo de alerta a todos los contactos
    fun sendMail(locationMessage: String) {
        viewModelScope.launch {
            try {
                checkEnvVars()
                Log.d("EmergencyVM", "🚀 Llamando a notifyAllContacts() desde ViewModel...")
                repository.notifyAllContacts(locationMessage)
                Log.d("EmergencyVM", "✅ notifyAllContacts() ejecutado correctamente")
            } catch (e: Exception) {
                Log.e("EmergencyVM", "❌ Error ejecutando notifyAllContacts: ${e.message}", e)
            }
        }
    }
    fun checkEnvVars() {
        Log.d("ENV_CHECK", "EMAIL_USERNAME=${BuildConfig.EMAIL_USERNAME}")
        Log.d("ENV_CHECK", "EMAIL_APP_PASSWORD length=${BuildConfig.EMAIL_APP_PASSWORD.length}")
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun createEmergency(){
        viewModelScope.launch {
       val res = emergencyRepository.insert(true)
            res.onSuccess { id -> _createdId.value= id }
                .onFailure { e ->
                    _error.value = e.message ?: "Error desconocido" }}

    }

}
