package app.code.petshopandroidapp.ui.model

data class User(
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val gender: String? = "",
    val isActive: Boolean,
    val createdAt: String? = ""
)