package app.code.petshopandroidapp.ui.screens.sell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.DetailLabel
import app.code.petshopandroidapp.ui.navigation.AppRoutes
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.BorderSubmitButtonColor
import app.code.petshopandroidapp.ui.viewmodel.SmartOrderViewModel
import app.code.petshopandroidapp.ui.viewmodel.SellViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartOrderScreen(navController: NavController, modifier: Modifier = Modifier) {
    val smartOrderViewModel: SmartOrderViewModel = hiltViewModel()
    val isProcessing by smartOrderViewModel.isProcessing.collectAsState()
    val error by smartOrderViewModel.error.collectAsState()
    val successMessage by smartOrderViewModel.successMessage.collectAsState()
    val cartItems by smartOrderViewModel.cartItems.collectAsState()
    val totalAmount by smartOrderViewModel.totalAmount.collectAsState()

    var textInput by remember { mutableStateOf("") }

    // Khi có cart items, lưu vào StateHolder và navigate về SellScreen
    LaunchedEffect(cartItems) {
        cartItems?.let { items ->
            if (items.isNotEmpty()) {
                try {
                    // Lưu cart items vào StateHolder để SellScreen có thể lấy
                    app.code.petshopandroidapp.ui.viewmodel.SmartOrderStateHolder.setPendingCartItems(items)
                    navController.popBackStack() // Quay về SellScreen
                } catch (e: Exception) {
                    // Handle error silently
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Đặt hàng thông minh") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        },
        containerColor = BackgroundColor2
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hướng dẫn
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💡 Hướng dẫn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BorderSubmitButtonColor
                    )
                    Text(
                        text = "Nhập câu nói tự nhiên để tạo đơn hàng.\nVí dụ:",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "• Bán 1 cái lồng cho chó\n• Tắm cho thú cưng",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Text input
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                label = { Text("Nhập câu nói của bạn...") },
                placeholder = { Text("Ví dụ: Bán 1 cái lồng cho chó") },
                maxLines = 5,
                enabled = !isProcessing,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = BorderSubmitButtonColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BorderSubmitButtonColor
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // TODO: Implement voice input
                        },
                        enabled = !isProcessing
                    ) {
                        Icon(
                            Icons.Default.Face,
                            contentDescription = "Ghi âm",
                            tint = if (isProcessing) Color.Gray else BorderSubmitButtonColor
                        )
                    }
                }
            )

            // Error message
            error?.let { errorMsg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "❌ $errorMsg",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

            // Success message
            successMessage?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "✅ $msg",
                            color = Color(0xFF2E7D32),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        cartItems?.let { items ->
                            Text(
                                text = "Đã thêm ${items.size} sản phẩm vào giỏ hàng",
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                        }
                        totalAmount?.let { amount ->
                            Text(
                                text = "Tổng tiền: ${String.format("%,d", amount)}₫",
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Submit button
            Button(
                onClick = {
                    smartOrderViewModel.processSmartOrder(textInput)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = textInput.isNotBlank() && !isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BorderSubmitButtonColor,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đang xử lý...")
                } else {
                    Text(
                        text = "Tạo đơn hàng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Clear button
            if (successMessage != null && cartItems == null) {
                TextButton(
                    onClick = {
                        textInput = ""
                        smartOrderViewModel.clearSuccessMessage()
                        smartOrderViewModel.clearError()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tạo đơn hàng mới")
                }
            }
        }
    }
}



