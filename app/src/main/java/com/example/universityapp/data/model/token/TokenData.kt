package com.example.universityapp.data.model.token

data class TokenData(
    val access_token: String,
    val expires_in: Int,
    val jti: String,
    val refresh_token: String,
    val scope: String,
    val token_type: String
)