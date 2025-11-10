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
import android.content.Context
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext

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
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>(LoginResult.Idle)
    val loginResult: LiveData<LoginResult> = _loginResult

    private val prefs = context.getSharedPreferences("user_cache", Context.MODE_PRIVATE)

    init {
        val savedUid = prefs.getString("uid", null)
        if (!savedUid.isNullOrEmpty()) {
            _loginResult.value = LoginResult.Success
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginModel.login(email, password) { success, errorMsg ->
                if (success) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        prefs.edit().putString("uid", it.uid).apply()
                    }

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

    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        prefs.edit().remove("uid").apply()
        _loginResult.value = LoginResult.Idle
    }
}