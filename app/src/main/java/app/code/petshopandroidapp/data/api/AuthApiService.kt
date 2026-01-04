package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.LoginRequest
import app.code.petshopandroidapp.data.model.LoginResponse
import app.code.petshopandroidapp.data.model.RegisterRequest
import app.code.petshopandroidapp.data.model.RegisterResponse
import app.code.petshopandroidapp.data.model.RegisterVerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/register/verify")
    suspend fun verifyRegister(@Body request: RegisterVerifyRequest): Response<RegisterResponse>
}