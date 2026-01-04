package app.code.petshopandroidapp.ui.screens.authentication

import android.content.Context
import android.icu.text.CaseMap.Title
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.AuthenticationComposable.Companion.OTPTextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.CancelButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SubmitButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleText
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.ItemBackgroundColor
import app.code.petshopandroidapp.ui.viewmodel.RegisterViewModel


@Composable
fun ForgetPasswordVerifyScreen(
    context: Context,
    navController: NavController,
//    viewModel: RegisterViewModel = RegisterViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 41.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Thay ảnh của bạn vào đây
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp, 200.dp)
                        // Kích thước ảnh

                )
                TitleText("Quên mật khẩu", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(13.dp))
                Box(
                    modifier = Modifier
                        .border(
                            2.dp,
                            color = BorderColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(color = ItemBackgroundColor),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 34.dp, start = 20.dp, end = 20.dp, bottom = 19.dp),
                    ) {
                        NormalText(text = "Nhập email của bạn")
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            label = "your@email.com",
                            onChangeValue = {}
                        )
                        Spacer(modifier = Modifier.height(19.5.dp))
                        NormalText(text = "Mã xác nhận gởi đến email của bạn")
                        Spacer(modifier = Modifier.height(9.dp))
                        OTPTextField(
                            label = "Mã xác nhận",
                            onChangeValue = {},
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CancelButton(
                                text = "Hủy",
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier.weight(1.4f)
                            )
                            Spacer(modifier = Modifier.weight(0.2f))
                            SubmitButton(
                                text = "Đăng ký",
                                onClick = {
                                    navController.navigate("Login")
                                },
                                modifier = Modifier.weight(1.4f)
                            )
                        }
                    }}
                }
            }


        }
    }

@Preview(showBackground = true)
@Composable
fun ForgetPasswordVerifyScreenPreview() {

    ForgetPasswordVerifyScreen(context = LocalContext.current, navController = rememberNavController())
}