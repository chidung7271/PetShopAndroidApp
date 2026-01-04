package app.code.petshopandroidapp.ui.screens.authentication
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
import androidx.compose.material3.MaterialTheme
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
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SmallText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SubmitButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextField
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleText
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.ItemBackgroundColor
import app.code.petshopandroidapp.ui.viewmodel.LoginViewModel
import app.code.petshopandroidapp.ui.viewmodel.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavController,
) {
    val viewModel: LoginViewModel = hiltViewModel();
    val username = viewModel.username.collectAsState().value
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var buttonError by remember { mutableStateOf("") }
    val password = viewModel.password.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val loginResult = viewModel.loginResult.collectAsState().value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LaunchedEffect(Unit) {
            viewModel.checkToken()
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 29.dp),
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
                TitleText("Đăng nhập vào Pet App Management", textAlign = TextAlign.Center)
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
                        NormalText(text = "Tên đăng nhập")
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            label = "Username",
                            onChangeValue = {viewModel.onUsernameChanged(it)
                                            usernameError = ""},
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        if( usernameError.isNotEmpty()) {
                            SmallText(text = usernameError, color = Color.Red)
                        }
                        Spacer(modifier = Modifier.height(17.5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            NormalText(text = "Mật khẩu")
                        }
                        Spacer(modifier = Modifier.height(9.dp))
                        TextField(
                            textFieldType = 0,
                            label = "Password",
                            onChangeValue = {viewModel.onPasswordChanged(it)
                                            passwordError = ""},
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        if (passwordError.isNotEmpty() || buttonError.isNotEmpty()) {
                            if (buttonError.isNotEmpty()) {
                                ButtonErrorMessage(
                                    error = buttonError,
                                    onDismiss = { buttonError = "" } // Reset error sau khi thông báo biến mất
                                )
                            } else {
                                SmallText(passwordError, color = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        SubmitButton(
                            text = "Đăng nhập",
                            onClick = {
//                                viewModel.login()
                                var hasError = false
                                if (username.isEmpty()) {
                                    usernameError = "Username không được để trống"
                                    hasError = true
                                } else if (username.length < 7){
                                    usernameError ="Username phải nhiều hơn 6 kí tự"
                                    hasError = true
                                } else if (username.contains(" ")){
                                    usernameError = "Username không được chứa dấu cách"
                                    hasError = true
                                }
                                if (password.isEmpty()) {
                                    passwordError = "Mật khẩu không được để trống."
                                    hasError = true
                                }
                                if (!hasError){
                                            viewModel.login()
                                    }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        LaunchedEffect(loginResult) {
                            if (loginResult?.success == true) {
                                navController.navigate("Home") {
                                    popUpTo("SignIn") { inclusive = true }
                                }
                            } else {
                                if (loginResult != null) {
                                    buttonError = loginResult.message.toString()
                                }
                            }
                        }

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText("Chưa có tài khoản?")
                    TextButton(onClick = {
                        navController.navigate("SignUp")
                    }, text = "Đăng ký ngay")

                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    LoginScreen(navController = rememberNavController())
}