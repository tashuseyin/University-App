package com.example.universityapp.data.remote

import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface UniversityApi {

    @POST("oauth/token")
    suspend fun getGlobalToken(@QueryMap queries: Map<String, String>): Response<TokenData>

    @GET("university")
    suspend fun getUniversityList(@Header("authorization") Authorization: String): Response<UniversityData>


}