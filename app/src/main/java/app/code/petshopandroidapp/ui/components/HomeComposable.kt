package app.code.petshopandroidapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import app.code.petshopandroidapp.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SmallText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleMediumText
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp


class HomeComposable {
    companion object {
        @Composable
        fun TopBar(
            text: String = "Ứng dụng PetShop",
            onInventoryClick: () -> Unit = {},
            onLogout: () -> Unit = {}
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var logoMenuExpanded by remember { mutableStateOf(false) }

                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.pethouse),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { logoMenuExpanded = true },
                        tint = Color.Unspecified
                    )

                    DropdownMenu(
                        expanded = logoMenuExpanded,
                        onDismissRequest = { logoMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Đăng xuất") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                logoMenuExpanded = false
                                onLogout()
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TitleMediumText(
                        text = text,
                    )
                }

                var menuExpanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "MoreVert",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Quản lý kho") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onInventoryClick()
                            }
                        )
                    }
                }
            }
        }

        @Composable
        fun LabelStatistics(
            ordersCount: Int = 0,
            productsCount: Int = 0,
            onOrdersClick: () -> Unit = {}
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 15.dp, start = 13.dp, end = 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ItemStatistics(
                    title = "Hóa đơn",
                    image = R.drawable.dollar,
                    num = ordersCount,
                    onClick = onOrdersClick
                )
                ItemStatistics(
                    title = "Sản phẩm",
                    image = R.drawable.food,
                    text = " - tổng số - ",
                    num = productsCount
                )
            }
        }

        @Composable
        fun ItemStatistics(
            title: String,
            image: Int,
            text: String = " - đã bán - ",
            num: Int,
            onClick: () -> Unit = {}
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 25.dp)
                    .size(width = 180.dp, height = 120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.3.dp, BorderColor, RoundedCornerShape(10.dp))
                    .background(color = Color.White)
                    .clickable(onClick = onClick)
            ) {
                Row(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Column(
                        Modifier.padding(top = 9.dp, bottom = 19.dp, start = 10.dp, end = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        NormalText(title)
                        Spacer(modifier = Modifier.height(3.dp))
                        SmallText(text)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = num.toString(),
                            style = InterFonts.numStatistics,
                        )


                    }
                    Box(
                        modifier = Modifier.padding(top = 33.dp),
                    ) {
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(57.dp)
                        )
                    }

                }

            }

        }


    }
}


    @Preview(showBackground = true)
    @Composable
    fun PreviewHome() {
        Column {
            HomeComposable.TopBar();


        }
//        val modelProducer = remember { CartesianChartModelProducer() }
//        // Use `runBlocking` only for previews, which don’t support asynchronous execution.
//        runBlocking {
//            modelProducer.runTransaction {
//                // Learn more: https://patrykandpatrick.com/eji9zq.
//                columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
//            }
//        }
//        RevenueChartScreen()

    }
