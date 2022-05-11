package com.example.universityapp.data.model.university_detail

data class Data(
    val detail: String? = "",
    val id: Int? = 0,
    val images: List<Image>? = listOf(),
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val majorDetail: List<MajorDetail>? = listOf(),
    val name: String? = "",
    val shortDetail: String? = "",
    val type: String? = "",
    val url: String? = ""
)