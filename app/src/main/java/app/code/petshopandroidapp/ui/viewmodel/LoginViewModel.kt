package app.code.petshopandroidapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.code.petshopandroidapp.data.repository.AuthRepository
import kotlin.text.Typography.dagger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.local.TokenManager
import app.code.petshopandroidapp.data.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)

    private val _username = MutableStateFlow("") // de ghi
    val username: StateFlow<String> = _username // de doc

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean?> = _isLoading


    fun onUsernameChanged(value: String) {
        _username.value = value
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
    }
    fun checkToken() {
        viewModelScope.launch {
            val token = tokenManager.getTokenOnce()
            Log.d("LoginViewModel", "Token: $token")
            if (token.isNotEmpty()) {
                isLoggedIn = true
            }
        }
    }
    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(username.value, password.value)
            if (result.success) {
                result.accessToken?.let { tokenManager.saveToken(it)
                    isLoggedIn = true
                }
            }
            _loginResult.value = result
            _isLoading.value = false
        }
    }
}
