package com.example.universityapp.data.model.token

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenData(
    val access_token: String? = "",
    val expires_in: Int? = 0,
    val jti: String? = "",
    val refresh_token: String? = "",
    val scope: String? = "",
    val token_type: String? = ""
) : Parcelable