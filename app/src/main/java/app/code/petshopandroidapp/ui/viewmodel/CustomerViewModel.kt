package app.code.petshopandroidapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.model.CustomerResponse
import app.code.petshopandroidapp.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _customer = MutableStateFlow<CustomerResponse?>(null)
    val customer: StateFlow<CustomerResponse?> = _customer.asStateFlow()

    private val _customers = MutableStateFlow<List<CustomerResponse>>(emptyList())
    val customers: StateFlow<List<CustomerResponse>> = _customers.asStateFlow()

    var id by mutableStateOf("")
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var gender by mutableStateOf("")
    var isActive by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        getAllCustomers()
    }

    fun getAllCustomers() {
        viewModelScope.launch {
            try {
                val result = customerRepository.getAllCustomers()
                _customers.value = result
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error getting customers: ${e.message}")
                errorMessage = "Lỗi khi tải danh sách khách hàng"
            }
        }
    }

    fun addCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    onError("Vui lòng nhập tên khách hàng")
                    return@launch
                }
                if (phone.isBlank()) {
                    onError("Vui lòng nhập số điện thoại")
                    return@launch
                }
                if (gender.isBlank()) {
                    onError("Vui lòng chọn giới tính")
                    return@launch
                }

                val customer = CustomerResponse(
                    name = name,
                    email = email,
                    phone = phone,
                    gender = gender,
                    isActive = true
                )

                val result = customerRepository.addCustomer(customer)
                if (result.success == true) {
                    getAllCustomers()
                    clearFields()
                    onSuccess()
                } else {
                    onError(result.message ?: "Lỗi khi thêm khách hàng")
                }
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error adding customer: ${e.message}")
                onError("Lỗi khi thêm khách hàng: ${e.message}")
            }
        }
    }

    private fun clearFields() {
        name = ""
        email = ""
        phone = ""
        gender = ""
        errorMessage = null
    }

    fun updateCustomer(
        customerId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // TODO: Implement updateCustomer
        }
    }

    fun deleteCustomer(
        customerId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = customerRepository.deleteCustomer(customerId)
                if (result.success == true) {
                    getAllCustomers()
                    onSuccess()
                } else {
                    onError(result.message ?: "Lỗi khi xóa khách hàng")
                }
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error deleting customer: ${e.message}")
                onError("Lỗi khi xóa khách hàng: ${e.message}")
            }
        }
    }

    fun getCustomerById(customerId: String) {
        viewModelScope.launch {
            try {
                val result = customerRepository.getCustomerById(customerId)
                _customer.value = result
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Error getting customer: ${e.message}")
                errorMessage = "Lỗi khi tải thông tin khách hàng"
            }
        }
    }
}