package com.example.universityapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _tokenResponse: MutableLiveData<Resource<TokenData>> = MutableLiveData()
    val tokenResponse get() = _tokenResponse

    suspend fun getGlobalToken() {
        _tokenResponse.value = Resource.Loading()
        try {
            val response = repository.getGlobalToken(applyTokenQueries())
            _tokenResponse.value = Resource.Success(response.body()!!)
        } catch (e: Exception) {
            _tokenResponse.value =
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }


    private fun applyTokenQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constant.QUERY_GRANT_TYPE] = "global"
        queries[Constant.QUERY_CLIENT_ID] = "testjwtclientid"
        queries[Constant.QUERY_CLIENT_SECRET] = "XY7kmzoNzl100"

        return queries
    }

}
