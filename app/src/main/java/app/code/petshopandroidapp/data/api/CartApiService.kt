package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.CartRequest
import app.code.petshopandroidapp.data.model.CartResponse
import retrofit2.Response
import retrofit2.http.*

interface CartApiService {
    @POST("cart")
    suspend fun createCart(@Body cart: CartRequest): Response<CartResponse>

    @GET("cart/{id}")
    suspend fun getCartById(@Path("id") id: String): Response<CartResponse>

    @GET("cart")
    suspend fun getAllCarts(): Response<List<CartResponse>>
} 