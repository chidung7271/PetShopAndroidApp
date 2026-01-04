package app.code.petshopandroidapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.code.petshopandroidapp.data.model.ServiceResponse
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText

class ServiceComposable {
    companion object {
        @Composable
        fun DetailService(
            title: String? = null,
            services: List<ServiceResponse>,
            onDeleteService: (String) -> Unit,
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                if (title != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NormalText(
                            "Tên dịch vụ",
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Mô tả",
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Giá",
                            modifier = Modifier.weight(1.5f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                } else {
                    NormalText("Danh sách dịch vụ", modifier = Modifier.padding(bottom = 10.dp))
                }

                services.forEach { service ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NormalText(
                            service.name.toString(),
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            service.des.toString(),
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "${service.price}₫",
                            modifier = Modifier.weight(1.5f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        IconButton(onClick = { onDeleteService(service.id.toString()) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
    }
}