package app.code.petshopandroidapp.ui.screens.sell

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.data.api.CartApiService
import app.code.petshopandroidapp.data.api.OrderApiService
import app.code.petshopandroidapp.data.model.CartItemResponse
import app.code.petshopandroidapp.data.model.CartResponse
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import java.time.format.DateTimeFormatter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import kotlinx.coroutines.launch

// ViewModel cho SellOrderDetails
@HiltViewModel
class SellOrderDetailsViewModel @Inject constructor(
    private val orderApiService: OrderApiService,
    private val cartApiService: CartApiService
) : ViewModel() {

    private val _cartData = MutableStateFlow<CartResponse?>(null)
    val cartData: StateFlow<CartResponse?> = _cartData

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadOrderDetails(orderId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Lấy chi tiết đơn hàng
                val orderResponse = orderApiService.getOrderById(orderId)
                if (orderResponse.isSuccessful) {
                    val order = orderResponse.body()
                    val cartId = order?.cartId
                    
                    if (cartId != null) {
                        // Lấy chi tiết giỏ hàng
                        val cartResponse = cartApiService.getCartById(cartId)
                        if (cartResponse.isSuccessful) {
                            _cartData.value = cartResponse.body()
                            Log.d("SellOrderDetailsVM", "Loaded cart with ${cartResponse.body()?.items?.size ?: 0} items")
                        } else {
                            _error.value = "Không thể tải chi tiết giỏ hàng"
                            Log.e("SellOrderDetailsVM", "Failed to load cart: ${cartResponse.code()}")
                        }
                    } else {
                        _error.value = "Không tìm thấy giỏ hàng cho đơn hàng này"
                    }
                } else {
                    _error.value = "Không thể tải chi tiết đơn hàng"
                    Log.e("SellOrderDetailsVM", "Failed to load order: ${orderResponse.code()}")
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                Log.e("SellOrderDetailsVM", "Error loading order details", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellOrderDetailsScreen(
    navController: NavController,
    orderId: String,
    modifier: Modifier = Modifier,
) {
    val detailsViewModel: SellOrderDetailsViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    
    val cartData = detailsViewModel.cartData.collectAsState().value
    val isLoading = detailsViewModel.isLoading.collectAsState().value
    val error = detailsViewModel.error.collectAsState().value
    val orders = homeViewModel.orders.collectAsState().value
    
    // Tìm order hiện tại
    val currentOrder = remember(orders, orderId) {
        orders.find { it.id == orderId }
    }

    LaunchedEffect(orderId) {
        Log.d("SellOrderDetailsScreen", "Loading order details for: $orderId")
        detailsViewModel.loadOrderDetails(orderId)
    }

    Scaffold(
        containerColor = BackgroundColor2,
        topBar = {
            SellOrderDetailsTopBar(
                orderCode = orderId.take(8),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
        } else if (cartData != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Thông tin đơn hàng
                item {
                    SellOrderDetailsCard(
                        order = currentOrder,
                        cartData = cartData
                    )
                }

                // Tiêu đề danh sách sản phẩm
                item {
                    Text(
                        text = "Danh sách sản phẩm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackColor
                    )
                }

                // Danh sách các sản phẩm
                items(cartData.items) { item ->
                    SellOrderProductCard(item = item)
                }

                // Tổng tiền
                item {
                    SellOrderTotalCard(totalAmount = cartData.totalAmount)
                }

                // Spacer để cuộn thoải mái
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không tìm thấy dữ liệu",
                    fontSize = 14.sp,
                    color = BlackColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellOrderDetailsTopBar(
    orderCode: String,
    onBackClick: () -> Unit
) {

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                modifier = Modifier,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    onClick = onBackClick,
//                    modifier = Modifier.width(40.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White,
//                        modifier = Modifier.width(24.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Hóa đơn #$orderCode",
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
        TopAppBar(
            title = { Text("Hóa đơn #$orderCode") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackgroundColor
            )
        )

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellOrderDetailsCard(
    order: app.code.petshopandroidapp.data.model.OrderResponse?,
    cartData: CartResponse
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mã hóa đơn:",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = order?.id?.take(16) ?: "N/A",
                    fontSize = 13.sp,
                    color = BlackColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ngày tạo:",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatOrderDate(order?.createdAt),
                    fontSize = 13.sp,
                    color = BlackColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Trạng thái:",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = getStatusLabel(order?.status ?: ""),
                    fontSize = 13.sp,
                    color = getStatusColor(order?.status ?: ""),
                    fontWeight = FontWeight.Bold
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng sản phẩm:",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${cartData.items.size} loại",
                    fontSize = 13.sp,
                    color = BlackColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SellOrderProductCard(item: CartItemResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Loại: ${getItemTypeLabel(item.type)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ID: ${item.itemId.take(12)}",
                        fontSize = 13.sp,
                        color = BlackColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 0.5.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Số lượng:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.quantity}",
                        fontSize = 13.sp,
                        color = BlackColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Giá:",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${formatCurrency(item.price * item.quantity)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SellOrderTotalCard(totalAmount: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tổng cộng:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${formatCurrency(totalAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getItemTypeLabel(type: String): String {
    return when (type.lowercase()) {
        "product" -> "Sản phẩm"
        "service" -> "Dịch vụ"
        else -> type
    }
}

fun formatCurrency(amount: Long): String {
    return String.format("%,d đ", amount)
}
