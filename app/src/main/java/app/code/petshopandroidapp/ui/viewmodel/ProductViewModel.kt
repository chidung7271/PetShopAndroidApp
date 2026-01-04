package app.code.petshopandroidapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.code.petshopandroidapp.data.repository.AuthRepository
import kotlin.text.Typography.dagger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.model.LoginResponse
import app.code.petshopandroidapp.data.model.ProductResponse
import app.code.petshopandroidapp.data.repository.ProductRepository
import app.code.petshopandroidapp.ui.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _product = MutableStateFlow<ProductResponse?>(null)
    val product: StateFlow<ProductResponse?> = _product.asStateFlow()


    var id by mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var name by mutableStateOf("")
    var category by mutableStateOf("")
    var quantity by mutableStateOf("")
    var price by mutableStateOf("")
    var des by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    fun uploadProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (selectedImageUri == null) {
                onError("Vui lòng chọn ảnh sản phẩm")
                return@launch
            }

            val result = productRepository.addProduct(
                imageUri = selectedImageUri!!,
                name = name,
                category = category,
                quantity = quantity.toIntOrNull() ?: 0,
                price = price.toIntOrNull() ?: 0,
                des = des,
                context = context
            )

            if (result.success) {
                onSuccess()
            } else {
                onError(result.message)
            }
        }
    }

    fun deleteProduct(
        productId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = productRepository.deleteProduct(
                id = productId
            )

            if (result.success) {
                onSuccess()
            } else {
                onError(result.message)
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            val result = productRepository.getProductById(productId)
            id = result.id
            result.imageUrl = result.imageUrl.replace("localhost", "10.0.2.2")
            _product.value = result
        }
    }
    fun updateProduct(
        productId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = productRepository.updateProduct(
                id = productId,
                imageUri = selectedImageUri,
                name = name.takeIf { it.isNotBlank() },
                category = category.takeIf { it.isNotBlank() },
                quantity = quantity.toIntOrNull(),
                price = price.toIntOrNull(),
                des = des.takeIf { it.isNotBlank() },
                context = context
            )

            if (result.success) {
                onSuccess()
            } else {
                onError(result.message)
            }
        }
    }
}


