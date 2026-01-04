package app.code.petshopandroidapp.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SmallText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleMediumText
import app.code.petshopandroidapp.ui.model.CartItem
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.viewmodel.SellViewModel
import coil.compose.AsyncImage

class SellComposable {
    companion object {
        @Composable
        fun TopBarSell(
            viewModel: SellViewModel,
            modifier: Modifier = Modifier
        ) {
            val query = viewModel.searchQuery
            val filtered by viewModel.filteredProducts.collectAsState()

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Bán hàng",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(11.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = query,
                        onValueChange = viewModel::updateQuery,
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .height(36.dp)
                                    .background(BackgroundColor2, shape = RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Search, null, Modifier.size(18.dp), Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                if (query.isEmpty()) {
                                    Text("Tìm sản phẩm hoặc dịch vụ", fontSize = 12.sp, color = Color.Gray)
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.weight(1.5f)
                    )
                }

                if (filtered.isNotEmpty()) {
                    LazyColumn {
                        items(filtered) { item ->
                            ListItem(
                                headlineContent = { Text(item.name) },
                                supportingContent = { 
                                    Text(
                                        when (item.type) {
                                            "PRODUCT" -> "Sản phẩm - ${item.price}₫"
                                            else -> "Dịch vụ - ${item.price}₫"
                                        }
                                    ) 
                                },
                                leadingContent = if (item.type == "PRODUCT" && item.imageUrl != null) {
                                    {
                                        AsyncImage(
                                            model = item.imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(4.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else null,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.addToCart(item)
                                        viewModel.updateQuery("") // clear sau khi chọn
                                    }
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun CartSection(
            cartItems: List<CartItem>,
            onQuantityChange: (String, Int) -> Unit,
            onRemoveItem: (String) -> Unit,
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText(
                        "Hình",
                        modifier = Modifier.weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    NormalText(
                        "Tên",
                        modifier = Modifier.weight(3f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    NormalText(
                        "SL",
                        modifier = Modifier.weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    NormalText(
                        "Giá",
                        modifier = Modifier.weight(2f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                }

                HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))

                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(60.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.type == "PRODUCT" && item.imageUrl != null) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .size(50.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .size(50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (item.type == "PRODUCT") "SP" else "DV")
                            }
                        }

                        NormalText(
                            item.name,
                            modifier = Modifier.weight(3f)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )

                        if (item.type == "PRODUCT") {
                            var tempQuantity by remember(item.quantity) { mutableStateOf(item.quantity.toString()) }
                            OutlinedTextField(
                                value = tempQuantity,
                                onValueChange = { newValue ->
                                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                        tempQuantity = newValue
                                        newValue.toIntOrNull()?.let { qty ->
                                            if (qty > 0) {
                                                onQuantityChange(item.id, qty)
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp),
                                textStyle = TextStyle(fontSize = 14.sp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        } else {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("1")
                            }
                        }

                        NormalText(
                            "${item.price}₫",
                            modifier = Modifier.weight(2f)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )

                        IconButton(onClick = { onRemoveItem(item.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.Red)
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBarSell() {
}