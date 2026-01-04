package app.code.petshopandroidapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.api.CustomerApiService
import app.code.petshopandroidapp.data.model.CustomerResponse
import app.code.petshopandroidapp.data.model.PetResponse
import app.code.petshopandroidapp.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val customerApiService: CustomerApiService
) : ViewModel() {

    private val _pets = MutableStateFlow<List<PetResponse>>(emptyList())
    val pets: StateFlow<List<PetResponse>> = _pets

    private val _customers = MutableStateFlow<List<CustomerResponse>>(emptyList())
    val customers: StateFlow<List<CustomerResponse>> = _customers

    var name by mutableStateOf("")
    var selectedCustomerId by mutableStateOf<String?>(null)
    var selectedCustomerName by mutableStateOf("")
    var type by mutableStateOf("")
    var breed by mutableStateOf("")
    var weight by mutableStateOf("")
    var des by mutableStateOf("")
    var selectedImageFile by mutableStateOf<File?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        getAllPets()
        getAllCustomers()
    }

    private fun getAllCustomers() {
        viewModelScope.launch {
            try {
                val response = customerApiService.getAllCustomers()
                if (response.isSuccessful) {
                    _customers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Không thể tải danh sách khách hàng"
            }
        }
    }

    fun getAllPets() {
        viewModelScope.launch {
            petRepository.getAllPets()
                .onSuccess { petList ->
                    _pets.value = petList
                }
                .onFailure { exception ->
                    errorMessage = exception.message
                }
        }
    }

    fun addPet(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        errorMessage = null

        if (name.isBlank() || selectedCustomerId == null || type.isBlank() || breed.isBlank() || 
            weight.isBlank() || des.isBlank() || selectedImageFile == null) {
            errorMessage = "Vui lòng điền đầy đủ thông tin và chọn ảnh"
            onError(errorMessage!!)
            return
        }

        val weightDouble = try {
            weight.toDouble()
        } catch (e: NumberFormatException) {
            errorMessage = "Cân nặng phải là số"
            onError(errorMessage!!)
            return
        }

        viewModelScope.launch {
            petRepository.createPet(
                name = name,
                ownerId = selectedCustomerId!!,
                type = type,
                breed = breed,
                weight = weightDouble,
                des = des,
                isActive = true,
                imageFile = selectedImageFile!!
            ).onSuccess {
                clearInputs()
                onSuccess()
            }.onFailure { exception ->
                errorMessage = exception.message
                onError(errorMessage ?: "Lỗi không xác định")
            }
        }
    }

    fun deletePet(
        petId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            petRepository.deletePet(petId)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { exception ->
                    onError(exception.message ?: "Lỗi khi xóa thú cưng")
                }
        }
    }

    private fun clearInputs() {
        name = ""
        selectedCustomerId = null
        selectedCustomerName = ""
        type = ""
        breed = ""
        weight = ""
        des = ""
        selectedImageFile = null
        errorMessage = null
    }
}