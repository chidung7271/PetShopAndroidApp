package app.code.petshopandroidapp.ui.components

import android.os.CountDownTimer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.SmallText
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.theme.BorderSubmitButtonColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.StrokeTextFormColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor
import kotlinx.coroutines.delay

class AuthenticationComposable {
    companion object {
        @Composable
        fun OTPTextField(
            label: String,
            onChangeValue: (String) -> Unit = {},

            ) {
            var isCodeSent by remember { mutableStateOf(false) }
            var countdownTime by remember { mutableIntStateOf(30) }
            var isSending by remember { mutableStateOf(false) }

            // CountDownTimer to handle waiting time for resend
            val timer = remember {
                object : CountDownTimer(30000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        countdownTime = (millisUntilFinished / 1000).toInt()
                    }

                    override fun onFinish() {
                        isSending = false
                        isCodeSent = false
                        countdownTime = 30
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(
                        singleLine = true,
                        maxLines = 1,
                        placeholder = { Text(text = label, style = InterFonts.TextNormal) },
                        value = text,
                        onValueChange = {
                            onChangeValue(it)
                            text = it
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = BorderSubmitButtonColor,
                            focusedIndicatorColor = BorderSubmitButtonColor,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = StrokeTextFormColor,
                        ),
                        modifier = Modifier
                            .height(52.2.dp)
                            .fillMaxWidth(),
                        textStyle = InterFonts.TextNormal,
                        shape = RoundedCornerShape(10.dp),
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd).padding(top = 0.5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSending) {
                            Text(
                                text = "Đã gửi mã (${countdownTime}s)",
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            ButtonOtp(
                                onClick = {
                                    if (!isCodeSent) {

                                        isSending = true
                                        isCodeSent = true
                                        timer.start()
                                    }
                                },
                                text = "Gửi lại mã",
                            )
                        }
                    }
                }
            }

        }
        @Composable
        fun ButtonOtp(
            text: String = "Lấy mã",
            onClick: () -> Unit = {},
            modifier: Modifier = Modifier
        )
        {
            Button(
                onClick = onClick,
                modifier = modifier
                    .height(46.dp) ,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubmitButtonColor,
                ),
                border = BorderStroke(2.dp, BorderSubmitButtonColor)
            ) {
                NormalText(text)
            }
        }

        @Composable
        fun ButtonErrorMessage(error: String, onDismiss: () -> Unit) {
            var showMessage by remember { mutableStateOf(false) }

            LaunchedEffect(error) {
                if (error.isNotEmpty()) {
                    showMessage = true
                    delay(2000) // Thời gian hiển thị thông báo
                    showMessage = false
                    onDismiss() // Gọi callback để reset error
                }
            }

            if (showMessage) {
                SmallText(error, color = Color.Red)
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun PreviewOTPTextField() {
    Column {
    AuthenticationComposable.OTPTextField(label = "Nhập mã OTP")
    AuthenticationComposable.ButtonOtp()
} }