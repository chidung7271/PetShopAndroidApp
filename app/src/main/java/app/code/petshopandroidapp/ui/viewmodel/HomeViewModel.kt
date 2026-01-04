package app.code.petshopandroidapp.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.data.api.CartApiService
import app.code.petshopandroidapp.data.api.OrderApiService
import app.code.petshopandroidapp.data.model.OrderResponse
import app.code.petshopandroidapp.data.model.ProductResponse
import app.code.petshopandroidapp.data.repository.ProductRepository
import app.code.petshopandroidapp.ui.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderApiService: OrderApiService,
    private val cartApiService: CartApiService
) : ViewModel() {

    private val _productsResponse = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<Product>> = _productsResponse
        .map { list ->
            list.map {
                Product(
                    id = it.id,
                    imageRes = it.imageUrl.replace("localhost", "10.0.2.2"),
                    name = it.name,
                    price = it.price.toInt()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders

    // Tổng số hóa đơn (orders) trong database
    private val _totalSoldItems = MutableStateFlow<Int>(0)
    val totalSoldItems: StateFlow<Int> = _totalSoldItems

    // Tổng số sản phẩm
    val totalProductsCount: StateFlow<Int> = _productsResponse
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    init {
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            try {
                // Load products và orders song song
                val productsDeferred = async { productRepository.getAllProducts() }
                val ordersDeferred = async {
                    val response = orderApiService.getAllOrders()
                    if (response.isSuccessful) {
                        response.body() ?: emptyList()
                    } else {
                        emptyList()
                    }
                }

                val productsResult = productsDeferred.await()
                val ordersResult = ordersDeferred.await()
                
                Log.d("HomeViewModel", "Loaded ${productsResult.size} products")
                Log.d("HomeViewModel", "Loaded ${ordersResult.size} orders")
                
                // Log chi tiết từng order
                ordersResult.forEachIndexed { index, order ->
                    Log.d("HomeViewModel", "Order[$index]: id=${order.id}, status=${order.status}, createdAt=${order.createdAt}, cartId=${order.cartId}")
                }
                
                _productsResponse.value = productsResult
                _orders.value = ordersResult

                // Tính tổng số hóa đơn (orders)
                calculateSoldItems()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    private suspend fun calculateSoldItems() {
        try {
            // Đếm tổng số orders (hóa đơn) trong database
            val totalOrders = _orders.value.size
            Log.d("HomeViewModel", "Total orders (hóa đơn): $totalOrders")
            _totalSoldItems.value = totalOrders
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error calculating orders count: ${e.message}", e)
        }
    }

    // Dữ liệu cho biểu đồ theo ngày (5 ngày gần nhất) - trả về doanh thu
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDailyStatistics(): Pair<List<String>, List<Long>> {
        Log.d("HomeViewModel", "getDailyStatistics: FUNCTION CALLED")
        
        // Chờ cho orders load xong (tối đa 5 giây)
        var attempts = 0
        while (_orders.value.isEmpty() && attempts < 50) {
            Log.d("HomeViewModel", "getDailyStatistics: Waiting for orders... attempt $attempts")
            kotlinx.coroutines.delay(100)
            attempts++
        }
        
        val startDate = LocalDate.now().minusDays(4)
        val dateLabels = (0..4).map { 
            startDate.plusDays(it.toLong()).format(DateTimeFormatter.ofPattern("dd/MM")) 
        }

        val allOrders = _orders.value
        Log.d("HomeViewModel", "getDailyStatistics: Total orders = ${allOrders.size}")
        
        // Tạm thời dùng tất cả orders (không filter completed) để test
        val ordersToUse = allOrders.filter { it.createdAt != null }
        Log.d("HomeViewModel", "getDailyStatistics: Orders with createdAt = ${ordersToUse.size}")
        
        // Parse createdAt với format "HH:mm dd/MM/yyyy" (ví dụ: "16:14 15/05/2025")
        val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        
        val ordersByDate = ordersToUse.mapNotNull { order ->
            order.createdAt?.let { createdAtStr ->
                try {
                    val dateTime = java.time.LocalDateTime.parse(createdAtStr, dateFormatter)
                    val dateKey = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    Log.d("HomeViewModel", "Order ${order.id} createdAt: $createdAtStr -> parsed date: $dateKey")
                    Pair(dateKey, order)
                } catch (e: DateTimeParseException) {
                    Log.e("HomeViewModel", "Error parsing date: $createdAtStr", e)
                    null
                }
            }
        }.groupBy({ it.first }, { it.second })

        // Tính doanh thu cho mỗi ngày (load cart để lấy totalAmount)
        val values = dateLabels.mapIndexed { index, _ ->
            val targetDate = startDate.plusDays(index.toLong())
            val dateKey = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val ordersOnDate = ordersByDate[dateKey] ?: emptyList()
            
            var totalRevenue = 0L
            ordersOnDate.forEach { order ->
                order.cartId?.let { cartId ->
                    try {
                        val cartResponse = cartApiService.getCartById(cartId)
                        if (cartResponse.isSuccessful) {
                            val totalAmount = cartResponse.body()?.totalAmount ?: 0L
                            totalRevenue += totalAmount
                        }
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error loading cart $cartId: ${e.message}")
                    }
                }
            }
            
            Log.d("HomeViewModel", "Date $dateKey: ${ordersOnDate.size} orders, revenue: $totalRevenue")
            totalRevenue
        }

        Log.d("HomeViewModel", "Daily statistics values (revenue): $values")
        return Pair(dateLabels, values)
    }

    // Dữ liệu cho biểu đồ theo tháng (5 tháng gần nhất) - trả về doanh thu
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMonthlyStatistics(): Pair<List<String>, List<Long>> {
        val months = mutableListOf<String>()
        val currentDate = LocalDate.now()
        
        (4 downTo 0).forEach { i ->
            val date = currentDate.minusMonths(i.toLong())
            months.add(date.format(DateTimeFormatter.ofPattern("MM/yyyy")))
        }

        // Tạm thời dùng tất cả orders
        // Parse createdAt với format "HH:mm dd/MM/yyyy"
        val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        
        val ordersByMonth = _orders.value
            .filter { it.createdAt != null }
            .mapNotNull { order ->
                order.createdAt?.let { createdAtStr ->
                    try {
                        val dateTime = java.time.LocalDateTime.parse(createdAtStr, dateFormatter)
                        val monthKey = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                        Pair(monthKey, order)
                    } catch (e: DateTimeParseException) {
                        null
                    }
                }
            }.groupBy({ it.first }, { it.second })

        val values = months.map { monthLabel ->
            // Convert MM/yyyy to YYYY-MM (monthLabel format: MM/yyyy)
            val parts = monthLabel.split("/")
            if (parts.size == 2) {
                val monthKey = "${parts[1]}-${parts[0].padStart(2, '0')}"
                val ordersInMonth = ordersByMonth[monthKey] ?: emptyList()
                
                // Tính doanh thu cho tháng này
                var totalRevenue = 0L
                ordersInMonth.forEach { order ->
                    order.cartId?.let { cartId ->
                        try {
                            val cartResponse = cartApiService.getCartById(cartId)
                            if (cartResponse.isSuccessful) {
                                val totalAmount = cartResponse.body()?.totalAmount ?: 0L
                                totalRevenue += totalAmount
                            }
                        } catch (e: Exception) {
                            Log.e("HomeViewModel", "Error loading cart $cartId: ${e.message}")
                        }
                    }
                }
                totalRevenue
            } else {
                0L
            }
        }

        return Pair(months, values)
    }

    // Dữ liệu cho biểu đồ theo năm (5 năm gần nhất) - trả về doanh thu
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getYearlyStatistics(): Pair<List<String>, List<Long>> {
        val currentYear = LocalDate.now().year
        val years = (currentYear - 4..currentYear).map { it.toString() }

        // Parse createdAt với format "HH:mm dd/MM/yyyy"
        val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        
        val ordersByYear = _orders.value
            .filter { it.createdAt != null }
            .mapNotNull { order ->
                order.createdAt?.let { createdAtStr ->
                    try {
                        val dateTime = java.time.LocalDateTime.parse(createdAtStr, dateFormatter)
                        val yearKey = dateTime.toLocalDate().year.toString()
                        Pair(yearKey, order)
                    } catch (e: DateTimeParseException) {
                        null
                    }
                }
            }.groupBy({ it.first }, { it.second })

        val values = years.map { year ->
            val ordersInYear = ordersByYear[year] ?: emptyList()
            
            // Tính doanh thu cho năm này
            var totalRevenue = 0L
            ordersInYear.forEach { order ->
                order.cartId?.let { cartId ->
                    try {
                        val cartResponse = cartApiService.getCartById(cartId)
                        if (cartResponse.isSuccessful) {
                            val totalAmount = cartResponse.body()?.totalAmount ?: 0L
                            totalRevenue += totalAmount
                        }
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error loading cart $cartId: ${e.message}")
                    }
                }
            }
            totalRevenue
        }

        return Pair(years, values)
    }
}

