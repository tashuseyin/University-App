package com.example.universityapp.data.model.university

data class UniversityItem(
    val id: Int? = 0,
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val name: String? = "",
    val shortDetail: String? = "",
    val type: String? = "",
    val url: String? = ""
)