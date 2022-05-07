package com.example.universityapp.data.model.university

data class UniversityModel(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String,
    val shortDetail: String,
    val type: String,
    val url: String
)