package com.example.universityapp.data.model.university

data class UniversityData(
    val data: List<UniversityItem>? = listOf(),
    val encrypted: Boolean? = false,
    val hashKey: String? = "",
    val messages: List<Message>? = listOf(),
    val status: Int? = 0
)