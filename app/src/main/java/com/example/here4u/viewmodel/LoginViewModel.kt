package com.example.here4u.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.domain.businesslogic.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class LoginResult {
    object Idle : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginModel: LoginModel,
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>(LoginResult.Idle)
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginModel.login(email, password) { success, errorMsg ->
                if (success) {

                    viewModelScope.launch {
                        try {
                            userRemoteRepository.updatelogindate()
                            _loginResult.postValue(LoginResult.Success)
                        } catch (e: Exception) {


                            e.printStackTrace()
                            _loginResult.postValue(LoginResult.Success)
                        }
                    }
                } else {
                    _loginResult.postValue(
                        LoginResult.Error(errorMsg ?: "User or Password are incorrect")
                    )
                }
            }
        }
    }
}
