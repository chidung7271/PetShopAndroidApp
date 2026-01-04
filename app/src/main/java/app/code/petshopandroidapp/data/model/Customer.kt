package app.code.petshopandroidapp.data.model

data class CreateCustomerResponse(
    val success: Boolean? = null,
    val message: String? = null
)

data class CustomerResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null
)