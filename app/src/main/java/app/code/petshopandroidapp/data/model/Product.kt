package app.code.petshopandroidapp.data.model


data class ProductResponse(
    val id: String,
    val name: String,
    val category: String,
    val quantity: Number,
    val price: Number,
    val des: String,
    var imageUrl: String,
    val createdAt: String,
)
data class CreateProductResponse(
    val success: Boolean,
    val message: String,
)