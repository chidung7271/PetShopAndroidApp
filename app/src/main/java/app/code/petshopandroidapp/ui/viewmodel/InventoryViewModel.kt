package app.code.petshopandroidapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.api.InventoryApiService
import app.code.petshopandroidapp.data.api.ProductApiService
import app.code.petshopandroidapp.data.api.ServiceApiService
import app.code.petshopandroidapp.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventorySearchItem(
    val id: String,
    val name: String,
    val type: String // "product" or "service"
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryApiService: InventoryApiService,
    private val productApiService: ProductApiService,
    private val serviceApiService: ServiceApiService
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<InventoryTransaction>>(emptyList())
    val transactions: StateFlow<List<InventoryTransaction>> = _transactions

    private val _alerts = MutableStateFlow<List<InventoryAlert>>(emptyList())
    val alerts: StateFlow<List<InventoryAlert>> = _alerts

    private val _stats = MutableStateFlow<InventoryStats?>(null)
    val stats: StateFlow<InventoryStats?> = _stats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // Search suggestions
    private val _searchSuggestions = MutableStateFlow<List<InventorySearchItem>>(emptyList())
    val searchSuggestions: StateFlow<List<InventorySearchItem>> = _searchSuggestions

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            loadTransactions()
            loadAlerts()
            loadStats()
        }
    }

    fun loadTransactions(itemType: String? = null, itemId: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = inventoryApiService.getAllTransactions(
                    itemType = itemType,
                    itemId = itemId,
                    limit = 100
                )

                if (response.isSuccessful) {
                    _transactions.value = response.body() ?: emptyList()
                    Log.d("InventoryViewModel", "Loaded ${_transactions.value.size} transactions")
                } else {
                    _error.value = "Failed to load transactions: ${response.code()}"
                    Log.e("InventoryViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("InventoryViewModel", "Exception loading transactions", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAlerts(threshold: Int = 10) {
        viewModelScope.launch {
            try {
                val response = inventoryApiService.getLowStockAlerts(threshold)
                if (response.isSuccessful) {
                    _alerts.value = response.body() ?: emptyList()
                    Log.d("InventoryViewModel", "Loaded ${_alerts.value.size} alerts")
                } else {
                    Log.e("InventoryViewModel", "Failed to load alerts: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("InventoryViewModel", "Exception loading alerts", e)
            }
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            try {
                val response = inventoryApiService.getInventoryStats()
                if (response.isSuccessful) {
                    _stats.value = response.body()
                    Log.d("InventoryViewModel", "Loaded stats: ${_stats.value}")
                } else {
                    Log.e("InventoryViewModel", "Failed to load stats: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("InventoryViewModel", "Exception loading stats", e)
            }
        }
    }

    fun importStock(
        itemType: String,
        itemId: String,
        quantity: Int,
        reason: String?,
        note: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val request = CreateInventoryTransactionRequest(
                    itemType = itemType,
                    itemId = itemId,
                    type = "import",
                    quantity = quantity,
                    reason = reason,
                    note = note
                )

                val response = inventoryApiService.importStock(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Nhập kho thành công"
                    loadAllData()
                } else {
                    _error.value = "Failed to import: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("InventoryViewModel", "Exception importing stock", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun exportStock(
        itemType: String,
        itemId: String,
        quantity: Int,
        reason: String?,
        note: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val request = CreateInventoryTransactionRequest(
                    itemType = itemType,
                    itemId = itemId,
                    type = "export",
                    quantity = quantity,
                    reason = reason,
                    note = note
                )

                val response = inventoryApiService.exportStock(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Xuất kho thành công"
                    loadAllData()
                } else {
                    _error.value = "Failed to export: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("InventoryViewModel", "Exception exporting stock", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun adjustStock(
        itemType: String,
        itemId: String,
        newQuantity: Int,
        reason: String?,
        note: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val request = AdjustInventoryRequest(
                    itemType = itemType,
                    itemId = itemId,
                    newQuantity = newQuantity,
                    reason = reason,
                    note = note
                )

                val response = inventoryApiService.adjustStock(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Điều chỉnh kho thành công"
                    loadAllData()
                } else {
                    _error.value = "Failed to adjust: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("InventoryViewModel", "Exception adjusting stock", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun searchItems(query: String, itemType: String) {
        viewModelScope.launch {
            try {
                if (query.isBlank()) {
                    _searchSuggestions.value = emptyList()
                    return@launch
                }

                val results = mutableListOf<InventorySearchItem>()

                // Tìm kiếm theo loại được chọn
                if (itemType == "product") {
                    val productResponse = productApiService.getAllProducts()
                    if (productResponse.isSuccessful) {
                        productResponse.body()?.forEach { product ->
                            if (product.name?.contains(query, ignoreCase = true) == true) {
                                results.add(
                                    InventorySearchItem(
                                        id = product.id ?: "",
                                        name = product.name ?: "",
                                        type = "product"
                                    )
                                )
                            }
                        }
                    }
                } else if (itemType == "service") {
                    val serviceResponse = serviceApiService.getAllServices()
                    if (serviceResponse.isSuccessful) {
                        serviceResponse.body()?.forEach { service ->
                            if (service.name?.contains(query, ignoreCase = true) == true) {
                                results.add(
                                    InventorySearchItem(
                                        id = service.id ?: "",
                                        name = service.name ?: "",
                                        type = "service"
                                    )
                                )
                            }
                        }
                    }
                }

                _searchSuggestions.value = results
            } catch (e: Exception) {
                Log.e("InventoryViewModel", "Error searching items", e)
                _searchSuggestions.value = emptyList()
            }
        }
    }

    fun clearSearchSuggestions() {
        _searchSuggestions.value = emptyList()
    }
}
