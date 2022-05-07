package com.example.universityapp.data.repository

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.remote.UniversityApi
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val api: UniversityApi
) : UniversityRepository {

    override suspend fun getGlobalToken(queries: Map<String, String>): TokenData {
        return api.getGlobalToken(queries)
    }

    override suspend fun getUniversityList(Authorization: String): UniversityData {
        return api.getUniversityList(Authorization)
    }
}