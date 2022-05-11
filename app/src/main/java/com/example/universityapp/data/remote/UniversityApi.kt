package com.example.universityapp.data.remote

import com.example.universityapp.common.Constant.QUERY_AUTHORIZATION
import com.example.universityapp.common.Constant.TOKEN_AND_LOGIN_URL
import com.example.universityapp.common.Constant.UNIVERSITY_DETAIL_URL
import com.example.universityapp.common.Constant.UNIVERSITY_URL
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.model.university_detail.UniversityDetailData
import retrofit2.Response
import retrofit2.http.*

interface UniversityApi {

    @POST(TOKEN_AND_LOGIN_URL)
    suspend fun getGlobalToken(
        @QueryMap queries: Map<String, String>
    ): Response<TokenData>

    @GET(UNIVERSITY_URL)
    suspend fun getUniversityList(
        @Header(QUERY_AUTHORIZATION) authorization: String
    ): Response<UniversityData>

    @GET(UNIVERSITY_DETAIL_URL)
    suspend fun getUniversityDetail(
        @Path("uniId") uniId: Int,
        @Header(QUERY_AUTHORIZATION) authorization: String
    ): Response<UniversityDetailData>
}
