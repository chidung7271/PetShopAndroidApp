package app.code.petshopandroidapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.api.OrderApiService
import app.code.petshopandroidapp.data.model.SmartOrderRequest
import app.code.petshopandroidapp.data.model.SmartOrderCartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SmartOrderViewModel @Inject constructor(
    private val orderApiService: OrderApiService
) : ViewModel() {

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _cartItems = MutableStateFlow<List<SmartOrderCartItem>?>(null)
    val cartItems: StateFlow<List<SmartOrderCartItem>?> = _cartItems

    private val _totalAmount = MutableStateFlow<Long?>(null)
    val totalAmount: StateFlow<Long?> = _totalAmount

    fun processSmartOrder(text: String, customerId: String? = null) {
        if (text.isBlank()) {
            _error.value = "Vui lòng nhập câu nói"
            return
        }

        viewModelScope.launch {
            try {
                _isProcessing.value = true
                _error.value = null
                _successMessage.value = null
                _cartItems.value = null
                _totalAmount.value = null

                val request = SmartOrderRequest(
                    text = text,
                    customerId = customerId
                )

                Log.d("SmartOrderViewModel", "Sending smart order request: $request")
                val response = orderApiService.createSmartOrder(request)
                Log.d("SmartOrderViewModel", "Response: ${response.isSuccessful}, code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        _successMessage.value = body.message
                        _cartItems.value = body.cartItems
                        _totalAmount.value = body.totalAmount
                        Log.d("SmartOrderViewModel", "Smart order processed successfully. Items: ${body.cartItems?.size}")
                    } else {
                        _error.value = body?.message ?: "Không thể xử lý đơn hàng"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SmartOrderViewModel", "Error response: $errorBody")
                    _error.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("SmartOrderViewModel", "Error processing smart order", e)
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
        _cartItems.value = null
        _totalAmount.value = null
    }
}



