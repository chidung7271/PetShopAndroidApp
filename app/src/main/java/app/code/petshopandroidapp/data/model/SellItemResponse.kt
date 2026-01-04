package app.code.petshopandroidapp.data.model

sealed class SellItemResponse {
    abstract val id: String?
    abstract val name: String?
    abstract val price: Number?
    abstract val type: String // "PRODUCT" or "SERVICE"

    data class ProductItem(
        override val id: String? = null,
        override val name: String? = null,
        override val price: Number? = null,
        val quantity: Number? = null,
        val image: String? = null
    ) : SellItemResponse() {
        override val type: String = "PRODUCT"
    }

    data class ServiceItem(
        override val id: String? = null,
        override val name: String? = null,
        override val price: Number? = null,
        val des: String? = null
    ) : SellItemResponse() {
        override val type: String = "SERVICE"
    }
} 