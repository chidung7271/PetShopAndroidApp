package app.code.petshopandroidapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val codeSent: Boolean = false,
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onUsernameChanged(value: String) {
        _username.value = value
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChanged(value: String) {
        _confirmPassword.value = value
    }

    fun onEmailChanged(value: String) {
        _email.value = value
    }

    fun onOtpChanged(value: String) {
        _otp.value = value
    }

    fun sendOtp() {
        viewModelScope.launch {
            if (_username.value.isBlank() || _password.value.isBlank() || _confirmPassword.value.isBlank() || _email.value.isBlank()) {
                _uiState.value = RegisterUiState(error = "Vui lòng điền đầy đủ thông tin")
                return@launch
            }
            if (_password.value != _confirmPassword.value) {
                _uiState.value = RegisterUiState(error = "Mật khẩu không khớp")
                return@launch
            }

            _uiState.value = RegisterUiState(isLoading = true)
            val response = authRepository.requestRegister(_username.value, _password.value, _email.value)
            _uiState.value = RegisterUiState(
                isLoading = false,
                codeSent = response.success,
                error = if (response.success) null else response.message ?: "Gửi mã thất bại",
            )
        }
    }

    fun confirmRegister() {
        viewModelScope.launch {
            if (_otp.value.isBlank()) {
                _uiState.value = RegisterUiState(error = "Vui lòng nhập mã xác nhận")
                return@launch
            }

            _uiState.value = RegisterUiState(isLoading = true)
            val response = authRepository.verifyRegister(_username.value, _password.value, _email.value, _otp.value)
            _uiState.value = RegisterUiState(
                isLoading = false,
                success = response.success,
                error = if (response.success) null else response.message ?: "Xác nhận thất bại",
            )
        }
    }
}
