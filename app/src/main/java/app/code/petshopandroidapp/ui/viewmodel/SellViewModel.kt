package app.code.petshopandroidapp.ui.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.api.CustomerApiService
import app.code.petshopandroidapp.data.api.CartApiService
import app.code.petshopandroidapp.data.api.OrderApiService
import app.code.petshopandroidapp.data.api.ProductApiService
import app.code.petshopandroidapp.data.api.ServiceApiService
import app.code.petshopandroidapp.data.model.CartItemRequest
import app.code.petshopandroidapp.data.model.CartRequest
import app.code.petshopandroidapp.data.model.CustomerResponse
import app.code.petshopandroidapp.data.model.OrderRequest
import app.code.petshopandroidapp.ui.model.CartItem
import app.code.petshopandroidapp.utils.BillGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productApiService: ProductApiService,
    private val serviceApiService: ServiceApiService,
    private val customerApiService: CustomerApiService,
    private val cartApiService: CartApiService,
    private val orderApiService: OrderApiService
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    private val _filteredItems = MutableStateFlow<List<CartItem>>(emptyList())
    val filteredProducts: StateFlow<List<CartItem>> = _filteredItems

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _customers = MutableStateFlow<List<CustomerResponse>>(emptyList())
    val customers: StateFlow<List<CustomerResponse>> = _customers

    private val _selectedCustomer = MutableStateFlow<CustomerResponse?>(null)
    val selectedCustomer: StateFlow<CustomerResponse?> = _selectedCustomer

    private val _showCustomerDialog = MutableStateFlow(false)
    val showCustomerDialog: StateFlow<Boolean> = _showCustomerDialog

    private val _processingPayment = MutableStateFlow(false)
    val processingPayment: StateFlow<Boolean> = _processingPayment

    private val _totalAmount = MutableStateFlow("0")
    val totalAmount: StateFlow<String> = _totalAmount

    private val _showCart = MutableStateFlow(false)
    val showCart: StateFlow<Boolean> = _showCart

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _billPath = MutableStateFlow<String?>(null)
    val billPath: StateFlow<String?> = _billPath

    init {
        loadCustomers()
    }

    private fun loadCustomers() {
        viewModelScope.launch {
            try {
                val response = customerApiService.getAllCustomers()
                if (response.isSuccessful) {
                    _customers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun showCustomerSelection() {
        _showCustomerDialog.value = true
    }

    fun hideCustomerSelection() {
        _showCustomerDialog.value = false
    }

    fun selectCustomer(customer: CustomerResponse) {
        _selectedCustomer.value = customer
        hideCustomerSelection()
    }

    private fun resetState() {
        _cartItems.value = emptyList()
        _selectedCustomer.value = null
        _showCustomerDialog.value = false
        _filteredItems.value = emptyList()
        _totalAmount.value = "0"
        _showCart.value = false
        _error.value = null
        searchQuery = ""
    }

    fun setShowCart(show: Boolean) {
        _showCart.value = show
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun processPayment() {
        if (_cartItems.value.isEmpty()) {
            _error.value = "Giỏ hàng trống"
            return
        }

        if (_selectedCustomer.value == null) {
            _error.value = "Vui lòng chọn khách hàng"
            return
        }

        viewModelScope.launch {
            try {
                _processingPayment.value = true
                _error.value = null

                // Generate bill first
                try {
                    Log.d("SellViewModel", "Generating bill")
                    Log.d("SellViewModel", "Customer details: ${_selectedCustomer.value}")
                    Log.d("SellViewModel", "Cart items: ${_cartItems.value}")
                    
                    val billGenerator = BillGenerator(context)
                    val billPath = billGenerator.generateBill(
                        customer = _selectedCustomer.value!!,
                        items = _cartItems.value,
                        totalAmount = _totalAmount.value
                    )
                    _billPath.value = billPath
                    Log.d("SellViewModel", "Bill generated successfully at: $billPath")
                } catch (e: Exception) {
                    Log.e("SellViewModel", "Error generating bill", e)
                    _error.value = "Không thể tạo hóa đơn: ${e.message}"
                    return@launch
                }

                // Create cart items
                val cartItems = _cartItems.value.map { item ->
                    CartItemRequest(
                        type = if (item.type == "PRODUCT") "product" else "service",
                        itemId = item.id,
                        quantity = item.quantity,
                        price = item.price.toLong()
                    )
                }

                // Create cart request
                val cartRequest = CartRequest(
                    customerId = _selectedCustomer.value?.id,
                    items = cartItems,
                    totalAmount = _totalAmount.value.replace(",", "").toLong()
                )

                // Create cart
                Log.d("SellViewModel", "Creating cart with request: $cartRequest")
                val cartResponse = cartApiService.createCart(cartRequest)
                Log.d("SellViewModel", "Cart response: ${cartResponse.isSuccessful}, code: ${cartResponse.code()}, body: ${cartResponse.body()}")
                
                if (!cartResponse.isSuccessful) {
                    val errorMsg = "Failed to create cart: ${cartResponse.code()} - ${cartResponse.message()}"
                    Log.e("SellViewModel", errorMsg)
                    throw Exception(errorMsg)
                }

                // Get cart ID from response
                val cartId = cartResponse.body()?.id ?: throw Exception("Cart ID is null")
                Log.d("SellViewModel", "Created cart with ID: $cartId")

                // Create order with cart ID
                val orderRequest = OrderRequest(
                    customerId = _selectedCustomer.value?.id,
                    cartId = cartId
                )

                Log.d("SellViewModel", "Creating order with request: $orderRequest")
                val orderResponse = orderApiService.createOrder(orderRequest)
                Log.d("SellViewModel", "Order response: ${orderResponse.isSuccessful}, code: ${orderResponse.code()}, body: ${orderResponse.body()}")

                if (!orderResponse.isSuccessful) {
                    val errorMsg = "Failed to create order: ${orderResponse.code()} - ${orderResponse.message()}"
                    Log.e("SellViewModel", errorMsg)
                    throw Exception(errorMsg)
                }

                Log.d("SellViewModel", "Order created successfully")
                // Reset all state after successful order
                resetState()
            } catch (e: Exception) {
                Log.e("SellViewModel", "Error during payment process", e)
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _processingPayment.value = false
            }
        }
    }

    fun updateQuery(newQuery: String) {
        searchQuery = newQuery
        viewModelScope.launch {
            try {
                val combinedResults = mutableListOf<CartItem>()

                // Tìm kiếm sản phẩm
                val productResponse = productApiService.getAllProducts()
                if (productResponse.isSuccessful) {
                    productResponse.body()?.forEach { product ->
                        if (product.name?.contains(newQuery, ignoreCase = true) == true) {
                            combinedResults.add(
                                CartItem(
                                    id = product.id ?: "",
                                    name = product.name,
                                    price = product.price ?: 0,
                                    imageUrl = product.imageUrl.replace("localhost", "10.0.2.2"),
                                    type = "PRODUCT"
                                )
                            )
                        }
                    }
                }

                // Tìm kiếm dịch vụ
                val serviceResponse = serviceApiService.getAllServices()
                if (serviceResponse.isSuccessful) {
                    serviceResponse.body()?.forEach { service ->
                        if (service.name?.contains(newQuery, ignoreCase = true) == true) {
                            combinedResults.add(
                                CartItem(
                                    id = service.id ?: "",
                                    name = service.name,
                                    price = service.price ?: 0,
                                    type = "SERVICE"
                                )
                            )
                        }
                    }
                }

                _filteredItems.value = if (newQuery.isBlank()) {
                    emptyList()
                } else {
                    combinedResults
                }
            } catch (e: Exception) {
                _filteredItems.value = emptyList()
            }
        }
    }

    fun addToCart(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existing = currentItems.find { it.id == item.id && it.type == item.type }
        
        if (existing != null) {
            if (existing.type == "PRODUCT") {
                existing.quantity += 1
                _cartItems.value = currentItems
                recalculateTotal()
            }
        } else {
            currentItems.add(item.copy())
            _cartItems.value = currentItems
            recalculateTotal()
        }
    }

    private fun recalculateTotal() {
        val total = _cartItems.value.sumOf { item -> 
            item.price.toLong() * item.quantity 
        }
        _totalAmount.value = String.format("%,d", total)
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.find { it.id == itemId }?.let { item ->
            if (item.type == "PRODUCT") {
                val newQuantity = quantity.coerceAtLeast(1)
                if (item.quantity != newQuantity) {
                    item.quantity = newQuantity
                    _cartItems.value = currentItems
                    recalculateTotal()
                }
            }
        }
    }

    fun removeItem(itemId: String) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
        recalculateTotal()
    }

    fun getTotal(): String = _totalAmount.value

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalAmount.value = "0"
    }

    fun clearError() {
        _error.value = null
    }

    fun addCartItemsFromSmartOrder(cartItems: List<app.code.petshopandroidapp.data.model.SmartOrderCartItem>) {
        try {
            val currentItems = _cartItems.value.toMutableList()
            
            cartItems.forEach { smartItem ->
                val cartItem = CartItem(
                    id = smartItem.id,
                    name = smartItem.name,
                    price = smartItem.price,
                    imageUrl = smartItem.imageUrl?.replace("localhost", "10.0.2.2"),
                    type = if (smartItem.type == "product") "PRODUCT" else "SERVICE",
                    quantity = smartItem.quantity
                )
                
                // Kiểm tra xem item đã tồn tại chưa
                val existing = currentItems.find { it.id == cartItem.id && it.type == cartItem.type }
                if (existing != null) {
                    // Nếu đã tồn tại, cộng thêm số lượng
                    existing.quantity += cartItem.quantity
                } else {
                    // Nếu chưa tồn tại, thêm mới
                    currentItems.add(cartItem)
                }
            }
            
            _cartItems.value = currentItems
            recalculateTotal()
            _showCart.value = true // Hiển thị cart sau khi thêm items
        } catch (e: Exception) {
            throw e
        }
    }
}


