package app.code.petshopandroidapp.data.di

import app.code.petshopandroidapp.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Gọi đồng bộ từ Flow, tránh việc sử dụng runBlocking
        val token = runBlocking { tokenManager.getTokenOnce() }

        // Kiểm tra token và thêm vào header nếu có
        val request = if (token.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}

