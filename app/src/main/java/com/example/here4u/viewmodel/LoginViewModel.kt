package com.example.here4u.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Representa el estado del resultado del login
sealed class LoginResult {
    object Idle : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginModel: LoginModel
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>(LoginResult.Idle)
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginModel.login(email, password) { success, errorMsg ->
                if (success) {
                    _loginResult.value = LoginResult.Success
                } else {

                    _loginResult.value = LoginResult.Error(errorMsg ?: "User or Password are incorrect")
                }
            }
        }
    }
}
