package app.code.petshopandroidapp.ui.screens.authentication

import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SubmitButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextField
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.ItemBackgroundColor
import app.code.petshopandroidapp.ui.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen(
    context: Context,
    navController: NavController,
    viewModel: RegisterViewModel = RegisterViewModel()
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
            Column {
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
                            .padding(top = 41.5.dp, start = 20.dp, end = 20.dp, bottom = 19.dp),
                    ) {
                        NormalText(text = "Tên đăng nhập")
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            label = "Username",
                            onChangeValue = {}
                        )
                        Spacer(modifier = Modifier.height(18.5.dp))
                        NormalText(text = "Mật khẩu")
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            label = "Password",
                            textFieldType = 0,
                            onChangeValue = {},
                        )
                        Spacer(modifier = Modifier.height(18.5.dp))
                        NormalText(text = "Xác nhận mật khẩu")
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            label = "Confirm Password",
                            textFieldType = 0,
                            onChangeValue = {},
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        SubmitButton(
                            text = "Đăng ký",
                            onClick = {
                                navController.navigate("Login")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText("Đã có tài khoản?")
                    TextButton(onClick = {
                        navController.navigate("SignUp")
                    }, text = "Đăng nhập")

                }
            }
            Image(
                painter = painterResource(id = R.drawable.logo), // Thay ảnh của bạn vào đây
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp, 160.dp)
                    .align(Alignment.TopCenter) // Kích thước ảnh
                    .offset(y = (-115).dp)
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {

    RegisterScreen(context = LocalContext.current, navController = rememberNavController())
}