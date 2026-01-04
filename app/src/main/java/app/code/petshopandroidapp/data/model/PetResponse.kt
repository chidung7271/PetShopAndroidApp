package app.code.petshopandroidapp.data.model

data class PetResponse(
    val id: String? = null,
    val ownerId: String? = null,
    val name: String? = null,
    val type: String? = null,
    val breed: String? = null,
    val weight: Double? = null,
    val des: String? = null,
    val image: String? = null,
    val isActive: Boolean? = null
)

data class CreatePetResponse(
    val success: Boolean,
    val message: String,
)