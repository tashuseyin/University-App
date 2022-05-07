package com.example.universityapp.data.repository

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface UniversityRepository {

    suspend fun getGlobalToken(@QueryMap queries: Map<String, String>): Response<TokenData>

    suspend fun getUniversityList(@Header("authorization") Authorization: String): Response<UniversityData>
}