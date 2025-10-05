package com.example.here4u.domain.businesslogic

import android.util.Patterns
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import javax.inject.Inject

class SignupModel @Inject constructor(
    private val userRepository: UserRemoteRepository
) {

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}\$")
        return passwordRegex.matches(password)
    }

    fun signup(
        email: String,
        name: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (!isEmailValid(email)) {
            onResult(false, "Not a valid email")
            return
        }

        if (!isPasswordValid(password)) {
            onResult(false, "Password must have at least 8 characters, one uppercase letter and one special character")
            return
        }

        userRepository.registerUser(email, password, name, onResult)
    }
}