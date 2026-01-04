package app.code.petshopandroidapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.model.ServiceResponse
import app.code.petshopandroidapp.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _service = MutableStateFlow<ServiceResponse?>(null)
    val service: StateFlow<ServiceResponse?> = _service.asStateFlow()

    private val _services = MutableStateFlow<List<ServiceResponse>>(emptyList())
    val services: StateFlow<List<ServiceResponse>> = _services.asStateFlow()

    var id by mutableStateOf("")
    var name by mutableStateOf("")
    var des by mutableStateOf("")
    var price by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    init {
        getAllServices()
    }

    fun getAllServices() {
        viewModelScope.launch {
            try {
                val result = serviceRepository.getAllServices()
                _services.value = result
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error getting services: ${e.message}")
                errorMessage = "Lỗi khi tải danh sách dịch vụ"
            }
        }
    }

    fun addService(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    onError("Vui lòng nhập tên dịch vụ")
                    return@launch
                }
                if (price.isBlank()) {
                    onError("Vui lòng nhập giá dịch vụ")
                    return@launch
                }

                val service = ServiceResponse(
                    name = name,
                    des = des,
                    price = price.toInt(),
                )

                val result = serviceRepository.addService(service)
                if (result.success == true) {
                    getAllServices()
                    clearFields()
                    onSuccess()
                } else {
                    onError(result.message ?: "Lỗi khi thêm dịch vụ")
                }
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error adding service: ${e.message}")
                onError("Lỗi khi thêm dịch vụ: ${e.message}")
            }
        }
    }

    private fun clearFields() {
        name = ""
        des = ""
        price = ""
        errorMessage = null
    }

    fun updateService(
        serviceId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    onError("Vui lòng nhập tên dịch vụ")
                    return@launch
                }
                if (price.isBlank()) {
                    onError("Vui lòng nhập giá dịch vụ")
                    return@launch
                }

                val service = ServiceResponse(
                    name = name,
                    des = des,
                    price = price.toInt(),
                )

                val result = serviceRepository.updateService(serviceId, service)
                if (result.success == true) {
                    getAllServices()
                    onSuccess()
                } else {
                    onError(result.message ?: "Lỗi khi cập nhật dịch vụ")
                }
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error updating service: ${e.message}")
                onError("Lỗi khi cập nhật dịch vụ: ${e.message}")
            }
        }
    }

    fun deleteService(
        serviceId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = serviceRepository.deleteService(serviceId)
                if (result.success == true) {
                    getAllServices()
                    onSuccess()
                } else {
                    onError(result.message ?: "Lỗi khi xóa dịch vụ")
                }
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error deleting service: ${e.message}")
                onError("Lỗi khi xóa dịch vụ: ${e.message}")
            }
        }
    }

    fun getServiceById(serviceId: String) {
        viewModelScope.launch {
            try {
                val result = serviceRepository.getServiceById(serviceId)
                _service.value = result
            } catch (e: Exception) {
                Log.e("ServiceViewModel", "Error getting service: ${e.message}")
                errorMessage = "Lỗi khi tải thông tin dịch vụ"
            }
        }
    }
}