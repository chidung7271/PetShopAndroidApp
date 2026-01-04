package app.code.petshopandroidapp.ui.navigation

sealed class AppRoutes(val route: String) {
    sealed class Authentication {
        object AuthGraph : AppRoutes("AuthGraph")
        object SignIn : AppRoutes("SignIn")
        object SignUp : AppRoutes("SignUp")
        object SignUpVerification : AppRoutes("SignUpVerification")
        object ForgetPassword : AppRoutes("ForgetPassword")
        object ResetPassword : AppRoutes("ResetPassword")
        object ForgetPasswordVerification : AppRoutes("ForgetPasswordVerification")
    }
    sealed class Main {
        object MainGraph : AppRoutes("MainGraph")
        object Home : AppRoutes("Home")
        object Product : AppRoutes("Product")
        object ProductAdd : AppRoutes("AddProduct")
        object ProductEdit : AppRoutes("EditProduct/{productId}")
        object Sell : AppRoutes("Sell")
        object SellHistory : AppRoutes("SellHistory")
        object SellOrderDetails : AppRoutes("SellOrderDetails/{orderId}")
        object SmartOrder : AppRoutes("SmartOrder")
        object Inventory : AppRoutes("Inventory")
        object Pet : AppRoutes("Pet")
        object PetAdd : AppRoutes("AddPet")
        object Customer : AppRoutes("Customer")
        object CustomerAdd : AppRoutes("AddCustomer")
        object Service : AppRoutes("Service")
        object ServiceAdd : AppRoutes("AddService")
    }
}