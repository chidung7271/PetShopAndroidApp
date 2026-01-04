package app.code.petshopandroidapp.data.model

data class CreateServiceResponse(
    val success: Boolean? = null,
    val message: String? = null
)

data class ServiceResponse(
    val id: String? = null,
    val name: String? = null,
    val des: String? = null,
    val price: Number? = null,
    val createdAt: String? = null
)