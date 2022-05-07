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
class SharedViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _tokenState: MutableLiveData<Resource<TokenData>> = MutableLiveData()
    val tokenState get() = _tokenState

    suspend fun getGlobalCoins() {
        _tokenState.value = Resource.Loading()
        try {
            val response = repository.getGlobalToken(applyTokenQueries())
            _tokenState.value = Resource.Success(response)
        } catch (e: Exception) {
            _tokenState.value =
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
