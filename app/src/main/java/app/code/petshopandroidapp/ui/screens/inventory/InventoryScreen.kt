package app.code.petshopandroidapp.ui.screens.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.data.model.InventoryAlert
import app.code.petshopandroidapp.data.model.InventoryStats
import app.code.petshopandroidapp.data.model.InventoryTransaction
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.Pink40
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import app.code.petshopandroidapp.ui.viewmodel.InventoryViewModel

@Composable
fun InventoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: InventoryViewModel = hiltViewModel()
    val transactions = viewModel.transactions.collectAsState().value
    val alerts = viewModel.alerts.collectAsState().value
    val stats = viewModel.stats.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Lịch sử", "Cảnh báo", "Thống kê")

    Scaffold(
        containerColor = BackgroundColor2,
        topBar = {
            InventoryTopBar(
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Pink40
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Content
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTab) {
                    0 -> TransactionsTab(
                        transactions = transactions,
                        viewModel = viewModel,
                        onImport = { itemType, itemId, quantity, reason, note ->
                            viewModel.importStock(itemType, itemId, quantity, reason, note)
                        },
                        onExport = { itemType, itemId, quantity, reason, note ->
                            viewModel.exportStock(itemType, itemId, quantity, reason, note)
                        },
                        onAdjust = { itemType, itemId, newQuantity, reason, note ->
                            viewModel.adjustStock(itemType, itemId, newQuantity, reason, note)
                        }
                    )
                    1 -> AlertsTab(alerts)
                    2 -> StatsTab(stats)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Quản lý kho") },
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

@Composable
fun TransactionsTab(
    transactions: List<InventoryTransaction>,
    viewModel: InventoryViewModel,
    onImport: (String, String, Int, String?, String?) -> Unit,
    onExport: (String, String, Int, String?, String?) -> Unit,
    onAdjust: (String, String, Int, String?, String?) -> Unit,
) {
            var showImportDialog by remember { mutableStateOf(false) }
            var showExportDialog by remember { mutableStateOf(false) }
            var showAdjustDialog by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxSize()) {
                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ActionButton(
                        text = "Nhập kho",
                        icon = Icons.Default.Add,
                        containerColor = Color(0xFF4CAF50),
                        onClick = { showImportDialog = true }
                    )
                    ActionButton(
                        text = "Xuất kho",
                        icon = Icons.Default.Close,
                        containerColor = Color(0xFFFF9800),
                        onClick = { showExportDialog = true }
                    )
                    ActionButton(
                        text = "Điều chỉnh",
                        icon = Icons.Default.Edit,
                        containerColor = Color(0xFF9C27B0),
                        onClick = { showAdjustDialog = true }
                    )
                }

                if (transactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có giao dịch nào", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transactions) { transaction ->
                            TransactionCard(transaction)
                        }
                    }
                }
            }

            if (showImportDialog) {
                InventoryActionDialog(
                    title = "Nhập kho",
                    confirmText = "Nhập",
                    viewModel = viewModel,
                    onConfirm = { itemType, itemId, quantity, reason, note ->
                        onImport(itemType, itemId, quantity, reason, note)
                        showImportDialog = false
                    },
                    onDismiss = { showImportDialog = false }
                )
            }

            if (showExportDialog) {
                InventoryActionDialog(
                    title = "Xuất kho",
                    confirmText = "Xuất",
                    viewModel = viewModel,
                    onConfirm = { itemType, itemId, quantity, reason, note ->
                        onExport(itemType, itemId, quantity, reason, note)
                        showExportDialog = false
                    },
                    onDismiss = { showExportDialog = false }
                )
            }

            if (showAdjustDialog) {
                InventoryActionDialog(
                    title = "Điều chỉnh kho",
                    confirmText = "Lưu",
                    viewModel = viewModel,
                    isAdjust = true,
                    onConfirm = { itemType, itemId, quantity, reason, note ->
                        onAdjust(itemType, itemId, quantity, reason, note)
                        showAdjustDialog = false
                    },
                    onDismiss = { showAdjustDialog = false }
                )
            }
}

@Composable
fun TransactionCard(transaction: InventoryTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = getTransactionTypeColor(transaction.type).copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getTransactionTypeIcon(transaction.type),
                            contentDescription = null,
                            tint = getTransactionTypeColor(transaction.type),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = transaction.itemName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlackColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = getTransactionTypeLabel(transaction.type),
                            fontSize = 12.sp,
                            color = getTransactionTypeColor(transaction.type)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${if (transaction.type == "import") "+" else "-"}${transaction.quantity}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = getTransactionTypeColor(transaction.type)
                    )
                    Text(
                        text = "${transaction.newQuantity} còn lại",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            if (transaction.reason != null || transaction.note != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))
                
                if (transaction.reason != null) {
                    Text(
                        text = "Lý do: ${transaction.reason}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                if (transaction.note != null) {
                    Text(
                        text = "Ghi chú: ${transaction.note}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.createdAt,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text)
    }
}

@Composable
fun InventoryActionDialog(
    title: String,
    confirmText: String,
    viewModel: InventoryViewModel,
    isAdjust: Boolean = false,
    onConfirm: (itemType: String, itemId: String, quantity: Int, reason: String?, note: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var itemType by remember { mutableStateOf("product") }
    var itemId by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var showSuggestions by remember { mutableStateOf(false) }

    val searchSuggestions by viewModel.searchSuggestions.collectAsState()

    // Tìm kiếm khi searchQuery thay đổi
    LaunchedEffect(searchQuery, itemType) {
        if (searchQuery.isNotBlank() && itemId.isBlank()) {
            viewModel.searchItems(searchQuery, itemType)
            showSuggestions = true
        } else {
            viewModel.clearSearchSuggestions()
            showSuggestions = false
        }
    }

    AlertDialog(
        onDismissRequest = {
            viewModel.clearSearchSuggestions()
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                val qty = quantityText.toIntOrNull()
                if (itemId.isBlank() || qty == null || qty <= 0) {
                    errorText = "Vui lòng nhập ID và số lượng hợp lệ"
                    return@TextButton
                }
                errorText = null
                viewModel.clearSearchSuggestions()
                onConfirm(itemType, itemId.trim(), qty, reason.takeIf { it.isNotBlank() }, note.takeIf { it.isNotBlank() })
            }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.clearSearchSuggestions()
                onDismiss()
            }) {
                Text("Hủy")
            }
        },
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Loại item
                Text(text = "Loại hàng", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = itemType == "product",
                        onClick = {
                            itemType = "product"
                            searchQuery = ""
                            itemId = ""
                            itemName = ""
                        },
                        label = { Text("Sản phẩm") }
                    )
                    FilterChip(
                        selected = itemType == "service",
                        onClick = {
                            itemType = "service"
                            searchQuery = ""
                            itemId = ""
                            itemName = ""
                        },
                        label = { Text("Dịch vụ") }
                    )
                }

                // Search field với autocomplete
                Text(text = "Tìm kiếm ${if (itemType == "product") "sản phẩm" else "dịch vụ"}", fontWeight = FontWeight.SemiBold)
                
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isBlank()) {
                                itemId = ""
                                itemName = ""
                            }
                        },
                        label = { Text("Nhập tên để tìm kiếm") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Hiển thị suggestions dropdown
                    if (showSuggestions && searchSuggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            LazyColumn {
                                items(searchSuggestions.size) { index ->
                                    val suggestion = searchSuggestions[index]
                                    TextButton(
                                        onClick = {
                                            itemId = suggestion.id
                                            itemName = suggestion.name
                                            searchQuery = suggestion.name
                                            showSuggestions = false
                                            viewModel.clearSearchSuggestions()
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = suggestion.name,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (index < searchSuggestions.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }

                // Hiển thị item đã chọn
                if (itemId.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "✓ Đã chọn:",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = itemName,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "ID: $itemId",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text(if (isAdjust) "Số lượng mới" else "Số lượng") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Lý do (tuỳ chọn)") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú (tuỳ chọn)") },
                    singleLine = true
                )

                if (errorText != null) {
                    Text(text = errorText!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        }
    )
}

@Composable
fun AlertsTab(alerts: List<InventoryAlert>) {
    if (alerts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Không có cảnh báo hết hàng", color = Color.Gray)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(alerts) { alert ->
                AlertCard(alert)
            }
        }
    }
}

@Composable
fun AlertCard(alert: InventoryAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0) // Light orange
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlackColor
                )
                Text(
                    text = "${alert.category} • ${alert.itemType}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Còn ${alert.currentQuantity} (Ngưỡng: ${alert.minThreshold})",
                    fontSize = 13.sp,
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun StatsTab(stats: InventoryStats?) {
    if (stats == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsCard(
                    title = "Tổng số giao dịch",
                    items = listOf(
                        "Nhập kho" to stats.totalImports.toString(),
                        "Xuất kho" to stats.totalExports.toString(),
                        "Bán hàng" to stats.totalSales.toString(),
                        "Điều chỉnh" to stats.totalAdjustments.toString()
                    )
                )
            }
            item {
                StatsCard(
                    title = "Tổng số lượng",
                    items = listOf(
                        "Đã nhập" to "${stats.importQuantity} sản phẩm",
                        "Đã xuất" to "${stats.exportQuantity} sản phẩm",
                        "Đã bán" to "${stats.salesQuantity} sản phẩm"
                    )
                )
            }
        }
    }
}

@Composable
fun StatsCard(title: String, items: List<Pair<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SubmitButtonColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = label, fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BlackColor
                    )
                }
                if (items.last() != (label to value)) {
                    Divider(
                        color = Color.LightGray,
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

fun getTransactionTypeIcon(type: String) = when (type) {
    "import" -> Icons.Default.Add
    "export" -> Icons.Default.Clear
    "sale" -> Icons.Default.ShoppingCart
    "adjustment" -> Icons.Default.Edit
    else -> Icons.Default.Info
}

fun getTransactionTypeColor(type: String) = when (type) {
    "import" -> Color(0xFF4CAF50) // Green
    "export" -> Color(0xFFFF9800) // Orange
    "sale" -> Color(0xFF2196F3) // Blue
    "adjustment" -> Color(0xFF9C27B0) // Purple
    else -> Color.Gray
}

fun getTransactionTypeLabel(type: String) = when (type) {
    "import" -> "Nhập kho"
    "export" -> "Xuất kho"
    "sale" -> "Bán hàng"
    "adjustment" -> "Điều chỉnh"
    "return" -> "Trả hàng"
    else -> type
}
