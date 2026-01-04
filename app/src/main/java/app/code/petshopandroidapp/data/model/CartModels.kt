package app.code.petshopandroidapp.data.model

data class CartItemRequest(
    val type: String,
    val itemId: String,
    val quantity: Int,
    val price: Long,
    val pet: String? = null
)

data class CartRequest(
    val customerId: String? = null,
    val items: List<CartItemRequest>,
    val totalAmount: Long
)

data class OrderRequest(
    val customerId: String? = null,
    val cartId: String,
    val status: String = "completed"
)

data class CartResponse(
    val id: String,
    val customerId: String?,
    val items: List<CartItemResponse>,
    val totalAmount: Long,
    val createdAt: String
)

data class CartItemResponse(
    val type: String,
    val itemId: String,
    val quantity: Int,
    val price: Long,
    val pet: String?
)

data class OrderResponse(
    val id: String?,
    val customerId: String?,
    val cartId: String?,
    val status: String?,
    val createdAt: String?
)

data class SmartOrderRequest(
    val text: String,
    val customerId: String? = null
)

data class SmartOrderCartItem(
    val id: String,
    val name: String,
    val price: Long,
    val imageUrl: String? = null,
    val type: String,
    val quantity: Int
)

data class SmartOrderItem(
    val name: String,
    val quantity: Int,
    val type: String,
    val itemId: String? = null,
    val price: Long? = null,
    val toppings: List<String>? = null
)

data class SmartOrderExtractedData(
    val customerName: String? = null,
    val items: List<SmartOrderItem>,
    val paymentMethod: String? = null
)

data class SmartOrderResponse(
    val success: Boolean,
    val message: String,
    val cartItems: List<SmartOrderCartItem>? = null,
    val totalAmount: Long? = null,
    val extractedData: SmartOrderExtractedData? = null
) 