package com.example.universityapp.data.repository

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import retrofit2.Response

interface UniversityRepository {

    suspend fun getGlobalToken(queries: Map<String, String>): Response<TokenData>

    suspend fun getUniversityList(authorization: String): Response<UniversityData>
}