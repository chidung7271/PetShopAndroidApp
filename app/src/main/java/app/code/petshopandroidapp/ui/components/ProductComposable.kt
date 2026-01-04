package app.code.petshopandroidapp.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleMediumText
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.ImagePickerBox
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarEditProduct
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.viewmodel.ProductViewModel
import coil.compose.rememberAsyncImagePainter

class ProductComposable {
    companion object {
        @Composable
        fun TopBar(text: String = "Thêm hàng hóa" , navController: NavController, viewModel: ProductViewModel) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                TitleMediumText("Thêm hàng hóa")
                }
                TextButton(
                    onClick = {
                        Log.d("ProductComposable", "onClick: ${viewModel.selectedImageUri}")
                        if (viewModel.selectedImageUri == null) {
                            viewModel.errorMessage = "Bạn chưa chọn ảnh"
                            return@TextButton
                        }
                        Log.d("ProductComposable", "${viewModel.name} ${viewModel.category} ${viewModel.quantity} ${viewModel.price} ${viewModel.des}")
                        viewModel.uploadProduct(
                            onSuccess = { navController.navigate("Product") },
                            onError = { viewModel.errorMessage = it }
                        )
                    },
                    text = "Lưu",
                    style = InterFonts.TextNormal
                )


            }
        }
        @Composable
        fun TopBarEditProduct(
            text: String = "Chỉnh sửa hàng hóa",
            navController: NavController,
            viewModel: ProductViewModel,
        ) {
            var expanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TitleMediumText(text)
                }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Cập nhật sản phẩm",
                                    color = Color(0xFF4CAF50) // Màu xanh lá
                                )
                            },
                            onClick = {
                                expanded = false
                                viewModel.updateProduct(
                                    productId = viewModel.id,
                                    onSuccess = { navController.navigate("Product") },
                                    onError = { viewModel.errorMessage = it }
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Xóa sản phẩm",
                                    color = Color.Red
                                )
                            },
                            onClick = {
                                expanded = false
                                viewModel.deleteProduct(
                                    productId = viewModel.id,
                                    onSuccess = { navController.navigate("Product") },
                                    onError = { viewModel.errorMessage = it }
                                )
                            }
                        )
                    }
                }
            }
        }

        @Composable
        fun TopBarProduct(text: String = "Hàng hóa") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                TitleMediumText(text = text,)


                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        @Composable
        fun ImagePickerBox(
            initialImageUrl: String?  = null,
            onImageSelected: (Uri) -> Unit
        ) {

            var imageUri by remember { mutableStateOf<Uri?>(null) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    imageUri = it
                    onImageSelected(it)
                }
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEFF6FF)) // xanh nhạt
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when {
                    imageUri != null -> {
                        // Người dùng đã chọn ảnh mới
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    initialImageUrl != null -> {
                        // Hiển thị ảnh cũ từ server
                        Image(
                            painter = rememberAsyncImagePainter(initialImageUrl),
                            contentDescription = "Initial Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        // Chưa có ảnh nào
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Pick Image",
                            tint = Color(0xFF007AFF),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        @Composable
        fun NumberInputField(
            text: String = "",
            label: String,
            onValueChange: (String) -> Unit
        ) {
            var text by remember { mutableStateOf(text) }

            OutlinedTextField(
                value = text,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        text = newValue
                        onValueChange(newValue)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = BorderColor,
                    focusedIndicatorColor = BorderColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = BorderColor, ),
                placeholder = { NormalText(label)  },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                textStyle = InterFonts.TextNormal,
            )
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun ComboBoxSell(
            defaultValue: String = "",
            onOptionSelected: (String) -> Unit
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf(defaultValue) }
            var options by remember { mutableStateOf(listOf("Option 1", "Option 2", "Option 3")) }
            var showAddOptionDialog by remember { mutableStateOf(false) }
            var newOption by remember { mutableStateOf("") }

            val colors = TextFieldDefaults.colors(
                focusedLabelColor = BorderColor,
                focusedIndicatorColor = BorderColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = BorderColor,
            )

            if (showAddOptionDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showAddOptionDialog = false },
                    confirmButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            if (newOption.isNotBlank()) {
                                options = options + newOption
                                newOption = ""
                            }
                            showAddOptionDialog = false
                        }) {
                            NormalText("Thêm")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            showAddOptionDialog = false
                        }) {
                            NormalText("Hủy")
                        }
                    },
                    text = {
                        OutlinedTextField(
                            value = newOption,
                            onValueChange = { newOption = it },
                            label = { NormalText("Thêm lựa chọn") }
                        )
                    }
                )
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { NormalText("Chọn mục") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(horizontal = 10.dp),
                    colors = colors,
                    textStyle = InterFonts.TextNormal,
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { NormalText(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                onOptionSelected(option)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { NormalText("+ Thêm lựa chọn") },
                        onClick = {
                            expanded = false
                            showAddOptionDialog = true
                        }
                    )
                }
            }
        }
    }}
@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    Column {
} }
