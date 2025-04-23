package app.code.petshopandroidapp.ui.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.theme.BlackColor
import app.code.petshopandroidapp.ui.theme.BorderSubmitButtonColor
import app.code.petshopandroidapp.ui.theme.CancelBorderColor
import app.code.petshopandroidapp.ui.theme.CancelColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.theme.StrokeTextFormColor
import app.code.petshopandroidapp.ui.theme.SubmitButtonColor

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
            ){
            Text(
                text = text,
                style = style
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

    }
}
@Preview(showBackground = true)
@Composable
fun CommonTextFieldPreview() {
    Column(){
    CommonComposable.TextField(label = "Yyyy", textFieldType = 0, onChangeValue = {})
    CommonComposable.SubmitButton(text = "Submit", onClick = {})
    }
}