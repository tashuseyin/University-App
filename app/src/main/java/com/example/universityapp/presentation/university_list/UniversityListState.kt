package com.example.universityapp.presentation.university_list

import com.example.universityapp.data.model.university.UniversityModel

data class UniversityListState(
    val isLoading: Boolean = false,
    val universityList: List<UniversityModel> = emptyList(),
    val errorMessage: String = ""
)
