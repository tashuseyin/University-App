package com.example.universityapp.data.model.university

data class UniversityData(
    val data: List<UniversityModel>,
    val encrypted: Boolean,
    val hashKey: String,
    val messages: List<Message>,
    val status: Int
)