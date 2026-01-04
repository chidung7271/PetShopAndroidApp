package app.code.petshopandroidapp.data.repository

import android.util.Log
import app.code.petshopandroidapp.data.api.ServiceApiService
import app.code.petshopandroidapp.data.model.CreateServiceResponse
import app.code.petshopandroidapp.data.model.ServiceResponse
import retrofit2.Response
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val serviceApiService: ServiceApiService
) {
    suspend fun getAllServices(): List<ServiceResponse> {
        val response: Response<List<ServiceResponse>> = serviceApiService.getAllServices()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun addService(service: ServiceResponse): CreateServiceResponse {
        val response = serviceApiService.addService(service)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateServiceResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun updateService(id: String, service: ServiceResponse): CreateServiceResponse {
        val response = serviceApiService.updateService(id, service)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateServiceResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun deleteService(id: String): CreateServiceResponse {
        val response = serviceApiService.deleteService(id)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateServiceResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun getServiceById(id: String): ServiceResponse {
        val response = serviceApiService.getServiceById(id)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            ServiceResponse("", "", "", 10,  "")
        }
    }
}