import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import app.code.petshopandroidapp.R
import app.code.petshopandroidapp.ui.components.config.botnavigation.AnimatedNavigationBar
import app.code.petshopandroidapp.ui.components.config.botnavigation.ButtonData
import app.code.petshopandroidapp.ui.screens.home.HomeScreen
import app.code.petshopandroidapp.ui.screens.product.ProductScreen
import app.code.petshopandroidapp.ui.screens.sell.SellScreen
import app.code.petshopandroidapp.ui.screens.customer.CustomerScreen
import app.code.petshopandroidapp.ui.screens.pet.PetScreen
import app.code.petshopandroidapp.ui.screens.service.ServiceScreen
import app.code.petshopandroidapp.ui.theme.BackgroundColor

@Composable
fun MainScreen(navController: NavHostController, currentRoute: String) {
    val bottomNavItems = listOf(
        ButtonData("Trang chủ", Icons.Default.Home),
        ButtonData("Sản phẩm", ImageVector.vectorResource(R.drawable.outline_package_2_24)),
        ButtonData("Bán hàng", Icons.Default.ShoppingCart),
        ButtonData("Thú cưng", ImageVector.vectorResource(R.drawable.baseline_pets_24)),
        ButtonData("Dịch vụ", Icons.Default.Call),
        ButtonData("Khách", Icons.Default.Person),
    )

    val routes = listOf(
        "Home","Product", "Sell", "Pet", "Service","Customer"
    )

    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                routes = routes,
                buttons = bottomNavItems,
                barColor = Color.White,
                circleColor = Color.White,
                selectedColor = BackgroundColor,
                unselectedColor = Color.Gray,
                navController = navController
            )
        }
    ) { innerPadding ->
        when (currentRoute) {
            "Home" -> HomeScreen(navController, modifier = Modifier.padding(innerPadding))
            "Product" -> ProductScreen(navController,modifier = Modifier.padding(innerPadding))
            "Sell" -> SellScreen(navController,modifier = Modifier.padding(innerPadding))
            "Customer" -> CustomerScreen(navController,modifier = Modifier.padding(innerPadding))
            "Service" -> ServiceScreen(navController,modifier = Modifier.padding(innerPadding))
            "Pet" -> PetScreen(navController,modifier = Modifier.padding(innerPadding))
//            "Pet" -> PetScreen(navController,modifier = Modifier.padding(innerPadding))
        }
    }
}
