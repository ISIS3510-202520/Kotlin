package com.example.here4u.viewmodel

import android.util.Log
import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.BuildConfig
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.data.local.repositories.EmergencyContactsLocalRepository
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import com.example.here4u.data.remote.repositories.EmergencyRequestRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.domain.businesslogic.LocationModelImpl
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val emergencyRepository: EmergencyRequestRemoteRepository,
    private val EmergencylocalRepository: EmergencyContactsLocalRepository,
    private val locationModelImpl: LocationModelImpl
) : ViewModel() {

    private val userId: String? = userRepository.getUserId()
    private val _createdId = MutableStateFlow<GeoPoint?>(null)
    val createdId: StateFlow<GeoPoint?> = _createdId

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _contacts = MutableLiveData<List<EmergencyContactEntity>>()
    val localcontacts: LiveData<List<EmergencyContactEntity>> get() = _contacts



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

    fun sendMail(locationMessage: GeoPoint?) {
        val location = "${locationMessage?.latitude},${locationMessage?.longitude}"
        viewModelScope.launch {
            try {
                checkEnvVars()
                Log.d("EmergencyVM", "Llamando a notifyAllContacts() desde ViewModel...")
                repository.notifyAllContacts(location)
                Log.d("EmergencyVM", "notifyAllContacts() ejecutado correctamente")
            } catch (e: Exception) {
                Log.e("EmergencyVM", "Error ejecutando notifyAllContacts: ${e.message}", e)
            }
        }
    }

    fun loadContacts() {
        viewModelScope.launch {
            if (userId != null) {
                repository.getAll()
                    .collect { list ->
                        Log.d("EmergencyVM", "Contactos cargados: ${list.size}")
                    }
            } else {
                Log.e("EmergencyVM", "No hay usuario logueado, no se pueden cargar contactos")
            }
        }
    }

    fun checkEnvVars() {
        Log.d("ENV_CHECK", "EMAIL_USERNAME=${BuildConfig.EMAIL_USERNAME}")
        Log.d("ENV_CHECK", "EMAIL_APP_PASSWORD length=${BuildConfig.EMAIL_APP_PASSWORD.length}")
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun createEmergency(): Boolean {
        return try {
            val res = emergencyRepository.insert(true)
            res.onSuccess { id ->
                _createdId.value = id
                sendMail(id)
            }.onFailure { e ->
                _error.value = e.message ?: "Error desconocido"
            }
            res.isSuccess
        } catch (e: Exception) {
            _error.value = e.message ?: "Error desconocido"
            false
        }
    }

    suspend fun addEmergencyContact(contact: EmergencyContactEntity): Boolean {
        return try {
                val success=repository.addEmergencyContact(contact)

            true
        } catch (e: Exception) {
            _error.value = "Failed to add contact: ${e.message}"
            Log.e("EmergencyContactsVM", "Error adding emergency contact", e)
            false
        }
    }

   fun loadLocalContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val contacts = EmergencylocalRepository.getAll()
            _contacts.postValue(contacts)
        }
    }

    suspend fun syncPendingContacts() {

        EmergencylocalRepository.syncPendingContacts(repository)



    }


}