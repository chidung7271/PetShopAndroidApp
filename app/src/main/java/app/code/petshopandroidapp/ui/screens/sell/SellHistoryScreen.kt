package app.code.petshopandroidapp.ui.screens.sell

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.data.model.OrderResponse
import app.code.petshopandroidapp.ui.navigation.AppRoutes
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val ordersRaw = viewModel.orders.collectAsState().value
    
    // Sắp xếp orders theo ngày mới nhất trước (descending)
    val orders = ordersRaw.sortedByDescending { order ->
        try {
            if (order.createdAt != null) {
                val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                LocalDateTime.parse(order.createdAt, dateFormatter)
            } else {
                LocalDateTime.MIN // Đưa orders không có createdAt xuống cuối
            }
        } catch (e: DateTimeParseException) {
            Log.e("SellHistoryScreen", "Error parsing date: ${order.createdAt}", e)
            LocalDateTime.MIN
        }
    }
    
    val isLoading = orders.isEmpty()

    LaunchedEffect(Unit) {
        Log.d("SellHistoryScreen", "Orders count: ${orders.size}")
    }

    Scaffold(
        containerColor = BackgroundColor2,
        topBar = {
            SellHistoryTopBar(
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
        } else if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có hóa đơn nào",
                    fontSize = 16.sp,
                    color = BlackColor
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    SellHistoryOrderCard(
                        order = order,
                        onOrderClick = {
                            navController.navigate(
                                "SellOrderDetails/${order.id}"
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellHistoryTopBar(
    onBackClick: () -> Unit
) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp)
//            .background(BackgroundColor)
//            .padding(horizontal = 16.dp),
//        contentAlignment = Alignment.CenterStart
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
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
//                    text = "Lịch sử bán hàng",
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
    TopAppBar(
        title = { Text("Lịch sử bán hàng") },
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
fun SellHistoryOrderCard(
    order: OrderResponse,
    onOrderClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) { onOrderClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Hóa đơn #${order.id?.take(8) ?: "N/A"}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ngày: ${formatOrderDate(order.createdAt)}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Trạng thái: ${getStatusLabel(order.status ?: "")}",
                        fontSize = 13.sp,
                        color = getStatusColor(order.status ?: "")
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View Details",
                    tint = SubmitButtonColor,
                    modifier = Modifier.width(24.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatOrderDate(dateString: String?): String {
    if (dateString == null) return "N/A"
    return try {
        val dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        val dateTime = java.time.LocalDateTime.parse(dateString, dateFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        dateString
    }
}

fun getStatusLabel(status: String): String {
    return when (status.lowercase()) {
        "completed" -> "Hoàn thành"
        "pending" -> "Chờ xử lý"
        "cancelled" -> "Đã hủy"
        else -> status
    }
}

fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> Color(0xFF4CAF50) // Green
        "pending" -> Color(0xFFFFC107) // Amber
        "cancelled" -> Color(0xFFF44336) // Red
        else -> Color.Gray
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SellHistoryScreenPreview() {
    val navController = rememberNavController()
    SellHistoryScreen(navController)
}
