package com.example.universityapp.data.repository

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.model.university_detail.UniversityDetailData
import retrofit2.Response

interface UniversityRepository {

    suspend fun getGlobalToken(
        queries: Map<String, String>
    ): Response<TokenData>

    suspend fun getUniversityList(
        authorization: String
    ): Response<UniversityData>

    suspend fun getUniversityDetail(
        uniId: Int,
        authorization: String
    ): Response<UniversityDetailData>
}