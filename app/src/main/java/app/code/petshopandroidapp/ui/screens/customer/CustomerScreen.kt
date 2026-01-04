package app.code.petshopandroidapp.ui.screens.customer

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
import app.code.petshopandroidapp.ui.components.CustomerComposable.Companion.DetailUser
import app.code.petshopandroidapp.ui.components.CustomerComposable.Companion.TopBarUser
import app.code.petshopandroidapp.ui.components.ProductComposable.Companion.TopBarProduct
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import app.code.petshopandroidapp.ui.theme.BackgroundColor2
import app.code.petshopandroidapp.ui.theme.LinkColor
import app.code.petshopandroidapp.ui.viewmodel.CustomerViewModel
import app.code.petshopandroidapp.ui.viewmodel.HomeViewModel

@Composable
fun CustomerScreen(navController: NavController, modifier: Modifier) {
    val viewModel: CustomerViewModel = hiltViewModel()
    val customers = viewModel.customers.collectAsState().value
    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor2,
        topBar = { TopBarUser() },
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate("AddCustomer") },
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
                if (customers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetailUser(
                        title = "anything",
                        users = customers,
                        modifier = modifier,
                        onDeleteUser = { id ->
                            viewModel.deleteCustomer(customerId = id,
                                onSuccess = {
                                    Log.d("CustomerScreen", "Customer deleted successfully")
                        
                                },
                                onError = { error ->
                                    Log.e("CustomerScreen", "Error deleting customer: $error")
                                }
                            )
                        },
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