package app.code.petshopandroidapp.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.DetailLabel
import app.code.petshopandroidapp.ui.components.HomeComposable.Companion.LabelStatistics
import app.code.petshopandroidapp.ui.components.HomeComposable.Companion.TopBar
import app.code.petshopandroidapp.ui.components.config.barchart.StatisticBarGraphWithModeSelector
import app.code.petshopandroidapp.ui.model.Product
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel
import app.code.petshopandroidapp.data.local.TokenManager
import app.code.petshopandroidapp.ui.navigation.AppRoutes
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }
    val products = viewModel.products.collectAsState().value
    val totalSoldItems = viewModel.totalSoldItems.collectAsState().value // Tổng số hóa đơn (orders)
    val productsCount = viewModel.totalProductsCount.collectAsState().value
    val orders = viewModel.orders.collectAsState().value
    
    // Log để debug
    LaunchedEffect(orders.size) {
        Log.d("HomeScreen", "Orders count: ${orders.size}")
        Log.d("HomeScreen", "Completed orders: ${orders.count { it.status == "completed" }}")
    }

    Scaffold(
        containerColor = BackgroundColor2,
        topBar = {
            TopBar(
                onInventoryClick = { navController.navigate(AppRoutes.Main.Inventory.route) },
                onLogout = {
                    coroutineScope.launch {
                        try {
                            Log.d("HomeScreen", "Logout clicked")
                            tokenManager.clearToken()
                            Log.d("HomeScreen", "Token cleared")
                            navController.navigate(AppRoutes.Authentication.SignIn.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                            Log.d("HomeScreen", "Navigation to SignIn completed")
                        } catch (e: Exception) {
                            Log.e("HomeScreen", "Logout error: ${e.message}", e)
                            Toast.makeText(context, "Lỗi đăng xuất: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding).padding(top = 40.dp )
                .fillMaxSize()
        ) {
            item {
                LabelStatistics(
                    ordersCount = totalSoldItems, // Tổng số hóa đơn
                    productsCount = productsCount,
                    onOrdersClick = { 
                        // Navigate đến màn hình SellHistory (lịch sử bán hàng)
                        navController.navigate(app.code.petshopandroidapp.ui.navigation.AppRoutes.Main.SellHistory.route)
                    }
                )
            }
            item {
                StatisticBarGraphWithModeSelector(
                    onGetDailyData = { viewModel.getDailyStatistics() },
                    onGetMonthlyData = { viewModel.getMonthlyStatistics() },
                    onGetYearlyData = { viewModel.getYearlyStatistics() }
                )
            }
            item {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetailLabel(
                        products = products,
                        modifier =modifier
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}