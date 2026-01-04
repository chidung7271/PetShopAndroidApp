package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.CreateCustomerResponse
import app.code.petshopandroidapp.data.model.CustomerResponse
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiService {
    @GET("customer")
    suspend fun getAllCustomers(): Response<List<CustomerResponse>>

    @POST("customer")
    suspend fun addCustomer(
        @Body customer: CustomerResponse
    ): Response<CreateCustomerResponse>

    @PATCH("customer/{id}")
    suspend fun updateCustomer(
        @Path("id") id: String,
        @Body customer: CustomerResponse
    ): Response<CreateCustomerResponse>

    @DELETE("customer/{id}")
    suspend fun deleteCustomer(@Path("id") id: String): Response<CreateCustomerResponse>

    @GET("customer/{id}")
    suspend fun getCustomerById(@Path("id") id: String): Response<CustomerResponse>
}