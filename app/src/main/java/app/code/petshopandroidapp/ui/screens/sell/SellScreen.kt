package app.code.petshopandroidapp.ui.screens.sell

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.DetailLabel
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.components.SellComposable.Companion.CartSection
import app.code.petshopandroidapp.ui.components.SellComposable.Companion.TopBarSell
import app.code.petshopandroidapp.ui.components.CustomerDialog
import app.code.petshopandroidapp.ui.navigation.AppRoutes
import app.code.petshopandroidapp.ui.model.Product
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel
import app.code.petshopandroidapp.ui.viewmodel.SellViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SellScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Sử dụng hiltViewModel() với key để đảm bảo cùng instance
    // Key này sẽ đảm bảo cả SellScreen và SmartOrderScreen dùng cùng ViewModel instance
    val viewModel: SellViewModel = hiltViewModel(key = "SellViewModel")
    val cartItems by viewModel.cartItems.collectAsState()
    val showCustomerDialog by viewModel.showCustomerDialog.collectAsState()
    val customers by viewModel.customers.collectAsState()
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val processingPayment by viewModel.processingPayment.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val showCart by viewModel.showCart.collectAsState()
    val error by viewModel.error.collectAsState()
    val billPath by viewModel.billPath.collectAsState()

    // Kiểm tra và load cart items từ SmartOrderStateHolder (từ Smart Order)
    LaunchedEffect(Unit) {
        val pendingItems = app.code.petshopandroidapp.ui.viewmodel.SmartOrderStateHolder.pendingCartItems
        if (pendingItems != null && pendingItems.isNotEmpty()) {
            viewModel.addCartItemsFromSmartOrder(pendingItems)
            // Clear sau khi đã load
            app.code.petshopandroidapp.ui.viewmodel.SmartOrderStateHolder.clearPendingCartItems()
        }
    }

    // Tự động hiển thị cart nếu có items nhưng showCart = false
    LaunchedEffect(cartItems.size, showCart) {
        if (cartItems.isNotEmpty() && !showCart) {
            kotlinx.coroutines.delay(50) // Delay nhỏ để đảm bảo state đã được update
            viewModel.setShowCart(true)
        }
    }

    // Show error if any
    error?.let { errorMessage ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
            LaunchedEffect(errorMessage) {
                // Clear error after 3 seconds
                kotlinx.coroutines.delay(3000)
                viewModel.clearError()
            }
        }
    }

    // Show bill if generated
    billPath?.let { path ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Đơn hàng đã được tạo thành công!",
                    color = Color.Green,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextButton(
                    onClick = {
                        try {
                            val file = File(path)
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("SellScreen", "Error opening PDF", e)
                        }
                    }
                ) {
                    Text("Xem hóa đơn")
                }
            }
        }
    }

    if (showCustomerDialog) {
        CustomerDialog(
            customers = customers,
            onCustomerSelected = viewModel::selectCustomer,
            onDismiss = viewModel::hideCustomerSelection
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarSell(viewModel = viewModel)
        },
        floatingActionButton = {
            if (!showCart) {
                FloatingActionButton(
                    onClick = { viewModel.setShowCart(true) },
                    containerColor = BackgroundColor
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Xem giỏ hàng")
                }
            }
        },
        containerColor = BackgroundColor2
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Smart Order Button
            if (!showCart) {
                Button(
                    onClick = { navController.navigate(AppRoutes.Main.SmartOrder.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SubmitButtonColor
                    )
                ) {
                    Text("💬 Đặt hàng thông minh (Smart Order)")
                }
            }
            
            if (showCart) {
                CartSection(
                    cartItems = cartItems,
                    onQuantityChange = { itemId, newQuantity ->
                        viewModel.updateQuantity(itemId, newQuantity)
                    },
                    onRemoveItem = { itemId ->
                        viewModel.removeItem(itemId)
                    },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị thông tin khách hàng đã chọn
                selectedCustomer?.let { customer ->
                    Text(
                        text = "Khách hàng: ${customer.name} - ${customer.phone}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                // Hiển thị tổng tiền
                Text(
                    text = "Tổng tiền: ${totalAmount}₫",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (processingPayment) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                if (selectedCustomer == null) {
                                    viewModel.showCustomerSelection()
                                } else {
                                    viewModel.processPayment()
                                }
                            },
                            enabled = cartItems.isNotEmpty() && !processingPayment
                        ) {
                            Text(if (selectedCustomer == null) "Chọn khách hàng" else "Thanh toán")
                        }
                        Button(
                            onClick = {
                                viewModel.setShowCart(false)
                                viewModel.clearCart()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            enabled = !processingPayment
                        ) {
                            Text("Hủy")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun S() {

}