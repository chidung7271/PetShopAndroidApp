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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.code.petshopandroidapp.data.model.PetResponse
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.NormalText
import coil.compose.AsyncImage

class PetComposable {
    companion object {
        @Composable
        fun DetailPet(
            title: String? = null,
            pets: List<PetResponse>,
            onDeletePet: (String) -> Unit,
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
                            "Ảnh",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Tên",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Loại",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Giống",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "Cân nặng",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                } else {
                    NormalText("Danh sách thú cưng", modifier = Modifier.padding(bottom = 10.dp))
                }

                pets.forEach { pet ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = pet.image?.replace("localhost", "10.0.2.2"),
                            contentDescription = "Pet image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        NormalText(
                            pet.name.toString(),
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            pet.type.toString(),
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            pet.breed.toString(),
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        NormalText(
                            "${pet.weight}kg",
                            modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                        )
                        IconButton(onClick = { onDeletePet(pet.id.toString()) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                        }
                    }

                    HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }
    }
}