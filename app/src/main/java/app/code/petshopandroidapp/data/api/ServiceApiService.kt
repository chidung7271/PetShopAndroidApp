package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.CreateServiceResponse
import app.code.petshopandroidapp.data.model.ServiceResponse
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiService {
    @GET("service")
    suspend fun getAllServices(): Response<List<ServiceResponse>>

    @POST("service")
    suspend fun addService(
        @Body service: ServiceResponse
    ): Response<CreateServiceResponse>

    @PATCH("service/{id}")
    suspend fun updateService(
        @Path("id") id: String,
        @Body service: ServiceResponse
    ): Response<CreateServiceResponse>

    @DELETE("service/{id}")
    suspend fun deleteService(@Path("id") id: String): Response<CreateServiceResponse>

    @GET("service/{id}")
    suspend fun getServiceById(@Path("id") id: String): Response<ServiceResponse>
}