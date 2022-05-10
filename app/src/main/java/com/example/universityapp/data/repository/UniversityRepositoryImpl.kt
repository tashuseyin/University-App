package com.example.universityapp.data.repository

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.remote.UniversityApi
import retrofit2.Response
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val api: UniversityApi
) : UniversityRepository {

    override suspend fun getGlobalToken(queries: Map<String, String>): Response<TokenData> {
        return api.getGlobalToken(queries)
    }

    override suspend fun getUniversityList(authorization: String): Response<UniversityData> {
        return api.getUniversityList(authorization)
    }
}