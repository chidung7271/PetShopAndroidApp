package app.code.petshopandroidapp.ui.model

data class CartItem(
    val id: String,
    val name: String,
    val price: Number,
    val imageUrl: String? = null,
    val type: String, // "PRODUCT" or "SERVICE"
    var quantity: Int = 1
) 