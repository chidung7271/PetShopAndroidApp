package app.code.petshopandroidapp.ui.screens

import MainScreen
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.data.local.TokenManager
import app.code.petshopandroidapp.ui.components.config.botnavigation.AnimatedNavigationBar
import app.code.petshopandroidapp.ui.components.config.botnavigation.ButtonData
import app.code.petshopandroidapp.ui.navigation.AppRoutes
import app.code.petshopandroidapp.ui.screens.home.HomeScreen
import app.code.petshopandroidapp.ui.screens.authentication.LoginScreen
import app.code.petshopandroidapp.ui.screens.authentication.RegisterScreen
import app.code.petshopandroidapp.ui.screens.authentication.RegisterVerifyScreen
import app.code.petshopandroidapp.ui.screens.customer.AddCustomerScreen
import app.code.petshopandroidapp.ui.screens.pet.AddPetScreen
import app.code.petshopandroidapp.ui.screens.product.AddProductScreen
import app.code.petshopandroidapp.ui.screens.product.EditProductScreen
import app.code.petshopandroidapp.ui.screens.service.AddServiceScreen
import app.code.petshopandroidapp.ui.screens.service.ServiceScreen
import app.code.petshopandroidapp.ui.screens.sell.SmartOrderScreen
import app.code.petshopandroidapp.ui.screens.sell.SellHistoryScreen
import app.code.petshopandroidapp.ui.screens.sell.SellOrderDetailsScreen
import app.code.petshopandroidapp.ui.screens.inventory.InventoryScreen
import app.code.petshopandroidapp.ui.viewmodel.LoginViewModel
// NavigationGraph.kt
@Composable
fun AllScreen(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination =  AppRoutes.Authentication.AuthGraph.route
    ) {
        // Graph cho authentication
        navigation(
            startDestination = AppRoutes.Authentication.SignIn.route,
            route = AppRoutes.Authentication.AuthGraph.route
        ) {
            composable(AppRoutes.Authentication.SignIn.route) {
                LoginScreen(navController)
            }
            composable(AppRoutes.Authentication.SignUp.route) {
                 RegisterScreen(context =  navController.context, navController = navController)
            }
            composable(AppRoutes.Authentication.SignUpVerification.route) {
                RegisterVerifyScreen(context = navController.context, navController = navController)
            }
        }

        navigation(
            startDestination = AppRoutes.Main.Home.route,
            route = AppRoutes.Main.MainGraph.route
        ) {
            composable(AppRoutes.Main.Home.route) {
                MainScreen(navController, "Home")
            }
            composable(AppRoutes.Main.Product.route) {
                MainScreen(navController, "Product")
            }
            composable(AppRoutes.Main.ProductAdd.route) {
                AddProductScreen(navController)
            }
            composable(AppRoutes.Main.Sell.route) {
                MainScreen(navController, "Sell")
            }
            composable(AppRoutes.Main.SellHistory.route) {
                SellHistoryScreen(navController)
            }
            composable(AppRoutes.Main.SellOrderDetails.route,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                if (orderId != null) {
                    SellOrderDetailsScreen(navController = navController, orderId = orderId)
                }
            }
            composable(AppRoutes.Main.SmartOrder.route) {
                SmartOrderScreen(navController)
            }
            composable(AppRoutes.Main.Inventory.route) {
                InventoryScreen(navController)
            }
            composable(AppRoutes.Main.ProductEdit.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType }) // hoặc IntType
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                if (productId != null) {
                    EditProductScreen(productId = productId, navController = navController)
                }
            }
            composable(AppRoutes.Main.Customer.route) {
                MainScreen(navController, "Customer")
            }
            composable(AppRoutes.Main.CustomerAdd.route) {
                AddCustomerScreen(navController)
            }
            composable(AppRoutes.Main.Service.route) {
                MainScreen(navController, "Service")
            }
            composable(AppRoutes.Main.ServiceAdd.route) {
                AddServiceScreen(navController)
            }
            composable(AppRoutes.Main.Pet.route) {
                MainScreen(navController, "Pet")
            }
            composable(AppRoutes.Main.PetAdd.route) {
                AddPetScreen(navController)
            }

            // Add other main screens here
        }
    }
}
