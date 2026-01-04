package app.code.petshopandroidapp.data.repository

import app.code.petshopandroidapp.data.api.AuthApiService
import app.code.petshopandroidapp.data.model.LoginRequest
import app.code.petshopandroidapp.data.model.LoginResponse
import app.code.petshopandroidapp.data.model.RegisterRequest
import app.code.petshopandroidapp.data.model.RegisterResponse
import app.code.petshopandroidapp.data.model.RegisterVerifyRequest
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun login(username: String, password: String): LoginResponse {
      return try {
          val response = authApiService.login(LoginRequest(username, password))
          if (response.isSuccessful) {
              response.body()!!
          } else {
              // Nếu API trả về lỗi HTTP (ví dụ 400), thì tự tạo 1 LoginResponse fail
              LoginResponse(success = false, message = "Something went wrong")
          }
      } catch (e: Exception) {
          LoginResponse(success = false, message = "Network error: ${e.localizedMessage}")
      }
    }

    suspend fun requestRegister(username: String, password: String, email: String): RegisterResponse {
        return try {
            val response = authApiService.register(RegisterRequest(username, password, email))
            if (response.isSuccessful) {
                response.body() ?: RegisterResponse(success = false, message = "Empty response")
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepository", "Register error: ${response.code()} - $errorBody")
                RegisterResponse(success = false, message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Register exception: ${e.message}")
            RegisterResponse(success = false, message = "Network error: ${e.localizedMessage}")
        }
    }

    suspend fun verifyRegister(username: String, password: String, email: String, code: String): RegisterResponse {
        return try {
            val response = authApiService.verifyRegister(RegisterVerifyRequest(username, password, email, code))
            if (response.isSuccessful) {
                response.body() ?: RegisterResponse(success = false, message = "Empty response")
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepository", "VerifyRegister error: ${response.code()} - $errorBody")
                RegisterResponse(success = false, message = "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "VerifyRegister exception: ${e.message}")
            RegisterResponse(success = false, message = "Network error: ${e.localizedMessage}")
        }
    }
}