package app.code.petshopandroidapp.data.repository

import android.util.Log
import app.code.petshopandroidapp.data.api.CustomerApiService
import app.code.petshopandroidapp.data.model.CreateCustomerResponse
import app.code.petshopandroidapp.data.model.CustomerResponse
import retrofit2.Response
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerApiService: CustomerApiService
) {
    suspend fun getAllCustomers(): List<CustomerResponse> {
        val response: Response<List<CustomerResponse>> = customerApiService.getAllCustomers()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun addCustomer(customer: CustomerResponse): CreateCustomerResponse {
        val response = customerApiService.addCustomer(customer)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateCustomerResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun updateCustomer(id: String, customer: CustomerResponse): CreateCustomerResponse {
        val response = customerApiService.updateCustomer(id, customer)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateCustomerResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun deleteCustomer(id: String): CreateCustomerResponse {
        val response = customerApiService.deleteCustomer(id)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CreateCustomerResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun getCustomerById(id: String): CustomerResponse {
        val response = customerApiService.getCustomerById(id)
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            CustomerResponse("", "", "", "", "", false, "")
        }
    }
}