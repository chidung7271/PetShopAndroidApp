package app.code.petshopandroidapp.ui.components

import android.os.Build
import android.provider.CalendarContract.Colors
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.DetailLabel
import app.code.petshopandroidapp.ui.components.HomeComposable.Companion.LabelStatistics
import app.code.petshopandroidapp.ui.components.config.barchart.StatisticBarGraphWithModeSelector
import app.code.petshopandroidapp.ui.components.config.botnavigation.AnimatedNavigationBar
import app.code.petshopandroidapp.ui.components.config.botnavigation.ButtonData
import app.code.petshopandroidapp.ui.model.Product
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.BorderSubmitButtonColor
import app.code.petshopandroidapp.ui.theme.CancelBorderColor
import app.code.petshopandroidapp.ui.theme.CancelColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.StrokeTextFormColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import coil.compose.AsyncImage


class CommonComposable {
    companion object {
        @Composable
        fun TextField(
            label: String,
            textFieldType: Int = 1,
            onChangeValue: (String) -> Unit = {},
        ) {

            var textString by remember { mutableStateOf("") }
            var showPassword by remember { mutableStateOf(false) }
            OutlinedTextField(
                singleLine = true,
                maxLines = 1,
                placeholder = { NormalText(label) },
                value = textString,
                onValueChange = {
                    onChangeValue(it)
                    textString = it
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = BorderSubmitButtonColor,
                    focusedIndicatorColor = BorderSubmitButtonColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = StrokeTextFormColor,
                ),
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth(),
                textStyle = InterFonts.TextNormal,
                visualTransformation = if (textFieldType == 0 && !showPassword)
                    PasswordVisualTransformation()
                else VisualTransformation.None,
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    if (textFieldType == 0) {
                        IconButton(onClick = {showPassword = !showPassword}) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    if (showPassword) R.drawable.show_password
                                    else R.drawable.hide_password
                                ),
                                contentDescription = "Hiding password image",
                                modifier = Modifier.size(24.dp,24.dp),
                                tint = BlackColor.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            )
        }
        @Composable
        fun NormalTextField(title: String, value: String = "", onChangeValue: (String) -> Unit){
            var textString by remember { mutableStateOf(value) }
            OutlinedTextField(
                value = textString,
                label = { NormalText(title) },
                onValueChange = {
                    textString = it
                    onChangeValue(it)
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = BorderColor,
                    focusedIndicatorColor = BorderColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = BorderColor,
                ),

                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 10.dp),
                textStyle = InterFonts.TextNormal,
                )
        }
        @Composable
        fun SubmitButton(
            text: String,
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
        ) {
            Button(
                onClick = onClick,
                modifier = modifier
                    .height(37.dp).then(modifier),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubmitButtonColor,
                ),
                border = BorderStroke(2.dp, BorderSubmitButtonColor)
            ) {
                Text(
                    text = text,
                    style = InterFonts.TextNormal,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        @Composable
        fun CancelButton(
            text: String,
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
        ) {
            Button(
                onClick = onClick,
                modifier = modifier
                    .height(37.dp).then(modifier),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CancelColor,
                ),
                border = BorderStroke(2.dp, CancelBorderColor)
            ) {
                Text(
                    text = text,
                    style = InterFonts.TextNormal,
                    color = BlackColor,
                )
            }
        }
        @Composable
        fun NormalText(
            text: String,
            style: TextStyle = InterFonts.TextNormal,
            modifier: Modifier = Modifier,
            ){
            Text(
                text = text,
                style = style,
                modifier = modifier,
            )

        }
        @Composable
        fun TitleText(
            text: String,
            style: TextStyle = InterFonts.TitleBold,
            textAlign: TextAlign ?= null,
            ){
            Text(
                text = text,
                style = style,
                textAlign = textAlign
            )

        }

        @Composable
        fun SmallText(
            text: String,
            style: TextStyle = InterFonts.SuperSmall,
            color: Color = Color.Unspecified,
        ){
            Text(
                text = text,
                style = style,
                maxLines = 1,
                color = color,
            )
        }

        @Composable
        fun TitleMediumText(
            text: String,
            style: TextStyle = InterFonts.TitleMedium,
            modifier: Modifier = Modifier,
        ) {
            Text(
                text = text,
                style = style,
                modifier = modifier
            )
        }
        @Composable
        fun TextButton(
            text: String,
            onClick: () -> Unit,
            style: TextStyle = InterFonts.TextNormal,
            color: Color = LinkColor,
            ){
            Text(
                text = text,
                style = style,
                modifier = Modifier.clickable { onClick() }.padding(3.dp),
                color = color
            )

        }


        @Composable
        fun DetailLabel(title: String? = null, products: List<Product>,
                        modifier: Modifier = Modifier, navController: NavController? = null) {
            var displayedProducts by remember { mutableStateOf(products) }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)

            ) {
                    Column(
                    ) { if (title == null) {
                        NormalText("Sản phẩm", modifier = Modifier.padding(10.dp))
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NormalText(
                                "Hình ảnh",
                                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                            )
                            NormalText(
                                "Tên sản phẩm",
                                modifier = Modifier.weight(3f).wrapContentWidth(Alignment.CenterHorizontally)
                            )
                            NormalText(
                                "Giá",
                                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                    }

                        displayedProducts.forEach { product ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp)
                                    .clickable {
                                        navController?.navigate("EditProduct/${product.id}")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.imageRes,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(40.dp)
                                )
                                NormalText(
                                    product.name,
                                    modifier = Modifier.weight(3f).wrapContentWidth(Alignment.CenterHorizontally)
                                )
                                NormalText(
                                    "${product.price}₫",
                                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }

                            HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                        }

                }
            }
        }

//        @Composable
//        fun BotNav(){
//            val buttons = listOf(
//                ButtonData("Trang chủ", Icons.Default.Home),
//                ButtonData("Sản phẩm", ImageVector.vectorResource(R.drawable.outline_package_2_24)),
//                ButtonData("Bán hàng", Icons.Default.ShoppingCart),
//                ButtonData("Thú cưng", ImageVector.vectorResource(R.drawable.baseline_pets_24)),
//                ButtonData("Khách hàng", Icons.Default.Person),
//            )
//            AnimatedNavigationBar(
//                routes = listOf(
//                    "home",
//                    "product",
//                    "sell",
//                    "pet",
//                    "customer"
//                ),
//                buttons = buttons,
//                barColor = Color.White,
//                circleColor = Color.White,
//                selectedColor = Color.Blue,
//                unselectedColor = Color.Gray,
//                navController = rememberNavController(),
//            )
//        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CommonTextFieldPreview() {
    Column(){
//    CommonComposable.TextField(label = "Yyyy", textFieldType = 0, onChangeValue = {})
//    CommonComposable.SubmitButton(text = "Submit", onClick = {})
        val sampleProducts = listOf(
            Product("1","http://10.0.2.2:3000/uploads/food1.jpg", "Thức ăn cho mèo", 50000),
            Product("2","http://10.0.2.2:3000/uploads/food1.jpg", "Xương gặm cho chó", 75000),
            Product("3","http://10.0.2.2:3000/uploads/food1.jpg", "Sữa tắm thú cưng", 120000)
        )
        LabelStatistics()
        StatisticBarGraphWithModeSelector()
    CommonComposable.DetailLabel("Dũng",sampleProducts)
    }
}