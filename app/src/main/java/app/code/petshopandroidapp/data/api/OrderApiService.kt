package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.OrderRequest
import app.code.petshopandroidapp.data.model.OrderResponse
import app.code.petshopandroidapp.data.model.SmartOrderRequest
import app.code.petshopandroidapp.data.model.SmartOrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {
    @POST("order")
    suspend fun createOrder(@Body order: OrderRequest): Response<OrderResponse>

    @GET("order/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<OrderResponse>

    @GET("order")
    suspend fun getAllOrders(): Response<List<OrderResponse>>

    @PATCH("order/{id}")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body status: Map<String, String>
    ): Response<OrderResponse>

    @POST("order/smart")
    suspend fun createSmartOrder(@Body request: SmartOrderRequest): Response<SmartOrderResponse>
} 