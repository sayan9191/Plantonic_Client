package co.`in`.plantonic.retrofit.models.auth

data class AuthResponseModel(
    val access_token: String,
    val token_type: String,
    val role: String
)