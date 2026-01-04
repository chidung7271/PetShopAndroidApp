package app.code.petshopandroidapp.ui.screens.pet

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.components.PetComposable.Companion.DetailPet
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.viewmodel.PetViewModel

@Composable
fun PetScreen(navController: NavController, modifier: Modifier) {
    val viewModel: PetViewModel = hiltViewModel()
    val pets = viewModel.pets.collectAsState().value

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor2,
        topBar = { TopBarProduct("Thú cưng") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddPet") },
                containerColor = BackgroundColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                if (pets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetailPet(
                        title = "anything",
                        pets = pets,
                        modifier = modifier,
                        onDeletePet = { id ->
                            viewModel.deletePet(
                                petId = id,
                                onSuccess = {
                                    Log.d("PetScreen", "Pet deleted successfully")
                                    viewModel.getAllPets()
                                },
                                onError = { error ->
                                    Log.e("PetScreen", "Error deleting pet: $error")
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}