package com.example.here4u.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.domain.businesslogic.SignupModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignupResult {
    object Idle : SignupResult()
    object Success : SignupResult()
    data class Error(val message: String) : SignupResult()
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupModel: SignupModel
) : ViewModel() {

    private val _signupResult = MutableLiveData<SignupResult>(SignupResult.Idle)
    val signupResult: LiveData<SignupResult> = _signupResult

    fun signup(email: String, name: String, password: String) {
        viewModelScope.launch {
            signupModel.signup(email, name, password) { success, message ->
                if (success) {
                    _signupResult.postValue(SignupResult.Success)
                } else {
                    _signupResult.postValue(SignupResult.Error(message ?: "unkown error"))
                }
            }
        }
    }
}
