package app.code.petshopandroidapp.data.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val accessToken: String? = null,
    val message: String? ,
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
)

data class RegisterResponse(
    val success: Boolean,
    val message: String? = null,
)

data class RegisterVerifyRequest(
    val username: String,
    val password: String,
    val email: String,
    val code: String,
)