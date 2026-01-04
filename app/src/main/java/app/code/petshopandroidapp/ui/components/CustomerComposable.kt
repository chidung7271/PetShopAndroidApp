package app.code.petshopandroidapp.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.data.model.CustomerResponse
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TextButton
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.TitleMediumText
import app.code.petshopandroidapp.ui.components.CustomerComposable.Companion.DetailUser
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.ImagePickerBox
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarEditProduct
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.model.Product
import app.code.petshopandroidapp.ui.model.User
import app.code.petshopandroidapp.ui.theme.BorderColor
import app.code.petshopandroidapp.ui.theme.InterFonts
import app.code.petshopandroidapp.ui.viewmodel.ProductViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

class CustomerComposable {
    companion object {
        @Composable
        fun DetailUser(
            title: String? = null,
            users: List<CustomerResponse>,
            onDeleteUser: (String) -> Unit,
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
                            "Tên",
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "SĐT",
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Giới tính",
                            modifier = Modifier.weight(1.5f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.width(40.dp)) // Chỗ cho icon xóa
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                } else {
                    NormalText("Danh sách người dùng", modifier = Modifier.padding(bottom = 10.dp))
                }

                users.forEach { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NormalText(
                            user.name.toString(),
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            user.phone.toString(),
                            modifier = Modifier.weight(2f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            user.gender.toString(),
                            modifier = Modifier.weight(1.5f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        IconButton(onClick = { onDeleteUser(user.id.toString()) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
        @Composable
        fun TopBarUser(text: String = "Khách hàng") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                TitleMediumText(text = text,)


                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }}
@Preview(showBackground = true)
@Composable
fun Preview () {
    Column {
        DetailUser(
            title = "Danh sách người dùng",
            users = listOf(
                CustomerResponse("1", "Nguyễn Văn A", "chidung7271@gmail.com","0987654321", "Nam", true, "2023-10-01"),
                CustomerResponse("2", "Trần Thị B", "chidung7271@gmail.com","0123456789", "Nữ", true, "2023-10-02")
            ),
            onDeleteUser = { userId -> Log.d("DeleteUser", "Xóa người dùng có ID: $userId") }
        )
} }
