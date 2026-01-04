package app.code.petshopandroidapp.ui.screens.product

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.code.petshopandroidapp.ui.components.CommonComposable.Companion.DetailLabel
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel

@Composable
fun ProductScreen(navController: NavController, modifier: Modifier) {
    val viewModel: HomeViewModel = hiltViewModel()
    val products = viewModel.products.collectAsState().value
    Log.d("ProductScreen", "products: $products")
    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor2,
        topBar = { TopBarProduct() },
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate("AddProduct") },
            containerColor = BackgroundColor) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

) {  padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetailLabel(
                        title = "anything",
                        products = products,
                        modifier = modifier,
                        navController = navController,
                    )
                }


            }

        }
    }
    }


@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {

}