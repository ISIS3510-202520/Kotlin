package com.example.here4u.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.local.cache.UserCacheManager
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.domain.businesslogic.LoginModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginResult {
    object Idle : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginModel: LoginModel,
    private val userRemoteRepository: UserRemoteRepository,
    private val localRepository: JournalLocalRepository,
    private val userCacheManager: UserCacheManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>(LoginResult.Idle)
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        if (userCacheManager.isUserLoggedIn()) {
            _loginResult.value = LoginResult.Success
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginModel.login(email, password) { success, errorMsg ->
                if (success) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        userCacheManager.saveUser(it)
                    }

                    viewModelScope.launch {
                        try {
                            userRemoteRepository.updatelogindate()
                            localRepository.updateCache()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
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

    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        userCacheManager.clear()
        _loginResult.value = LoginResult.Idle
    }
}
