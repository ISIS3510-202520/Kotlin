package com.example.here4u.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.here4u.model.ForgotPasswordModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class ForgotPasswordResult {
    object Loading : ForgotPasswordResult()
    data class Success(val message: String) : ForgotPasswordResult()
    data class Error(val message: String) : ForgotPasswordResult()
}

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordModel: ForgotPasswordModel
) : ViewModel() {

    private val _forgotPasswordResult = MutableLiveData<ForgotPasswordResult>()
    val forgotPasswordResult: LiveData<ForgotPasswordResult> = _forgotPasswordResult

    fun sendPasswordReset(email: String) {
        _forgotPasswordResult.value = ForgotPasswordResult.Loading

        forgotPasswordModel.sendPasswordResetEmail(email) { success, message ->
            if (success) {
                _forgotPasswordResult.value = ForgotPasswordResult.Success(message ?: "Check your inbox")
            } else {
                _forgotPasswordResult.value = ForgotPasswordResult.Error(message ?: "Unknown error")
            }
        }
    }
}
