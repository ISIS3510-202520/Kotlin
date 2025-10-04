package com.example.here4u.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EmergencyContactsViewModel @Inject constructor(
    private val repository: EmergencyContactRemoteRepository,
    private val userRepository: UserRemoteRepository // 👈 inyectamos también el repo de usuario
) : ViewModel() {

    private val userId: String? = userRepository.getUserId()

    val contacts: StateFlow<List<EmergencyContactRemote>> =
        if (userId != null) {
            repository.getAll() // 👈 ahora sí pasamos el id del usuario
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
        } else {
            // Si no hay usuario logueado devolvemos un flow vacío
            kotlinx.coroutines.flow.flowOf(emptyList<EmergencyContactRemote>())
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
        }
}
