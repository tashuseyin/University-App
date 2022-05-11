package com.example.universityapp.data.model.university_detail

data class UniversityDetailData(
    val data: Data? = Data(),
    val encrypted: Boolean? = false,
    val hashKey: String? = "",
    val messages: List<Message>? = listOf(),
    val status: Int? = 0
)