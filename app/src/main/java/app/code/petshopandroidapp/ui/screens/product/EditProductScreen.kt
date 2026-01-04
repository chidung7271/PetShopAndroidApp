package app.code.petshopandroidapp.ui.screens.product

import androidx.compose.ui.graphics.Color
import android.content.Context
import android.icu.text.CaseMap.Title
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.AuthenticationComposable.Companion.ButtonErrorMessage
import app.code.petshopandroidapp.ui.components.AuthenticationComposable.Companion.OTPTextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalTextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SmallText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SubmitButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleText
import app.code.petshopandroidapp.ui.components.ProductComposable
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.ComboBoxSell
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.ImagePickerBox
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.NumberInputField
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBar
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarEditProduct
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.ItemBackgroundColor
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel
import app.code.petshopandroidapp.ui.viewmodel.LoginViewModel
import app.code.petshopandroidapp.ui.viewmodel.ProductViewModel
import app.code.petshopandroidapp.ui.viewmodel.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@Composable
fun EditProductScreen(
    productId: String,
    navController: NavController,

) {
    val viewModel: ProductViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
    }
    val product by viewModel.product.collectAsState()

    Scaffold(
        topBar = { TopBarEditProduct(navController = navController, viewModel = viewModel) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 40.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            if (product != null) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 29.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            ImagePickerBox(
                                initialImageUrl = product!!.imageUrl,
                                onImageSelected = { uri -> viewModel.selectedImageUri = uri },
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            NormalTextField(
                                value = product!!.name,
                                title = "Tên sản phẩm",
                                onChangeValue = { viewModel.name = it },
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            ComboBoxSell(
                                defaultValue = product!!.category,
                                onOptionSelected = { viewModel.category = it }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            NumberInputField(
                                text = product!!.quantity.toString(),
                                label = "Số lượng",
                                onValueChange = { viewModel.quantity = it },
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            NumberInputField(
                                text = product!!.price.toString(),
                                label = "Giá bán (VND)",
                                onValueChange = { viewModel.price = it },
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            NormalTextField(
                                value = product!!.des,
                                title = "Mô tả sản phẩm",
                                onChangeValue = { viewModel.des = it },
                            )
                        }
                    }
                }
            } else {
                // Hiển thị loading nếu cần
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
    }

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {


}