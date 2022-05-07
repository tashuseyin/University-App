package com.example.universityapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getGlobalToken()
        }
    }

    private val _tokenState: MutableLiveData<Resource<TokenData>> = MutableLiveData()
    val tokenState get() = _tokenState

    private suspend fun getGlobalToken() {
        _tokenState.value = Resource.Loading()
        try {
            val response = repository.getGlobalToken(applyTokenQueries())
            _tokenState.value = handleGlobalToken(response)
        } catch (e: Exception) {
            _tokenState.value =
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    private suspend fun handleGlobalToken(response: Response<TokenData>): Resource<TokenData> {
        return when {
            response.message().toString().contains("timeout") -> {
                Resource.Error("Timeout")
            }
            response.code() == 401 -> {
                getGlobalToken()
                Resource.Loading()
            }
            response.isSuccessful -> {
                val globalToken = response.body()
                Resource.Success(globalToken!!)
            }
            else -> {
                Resource.Error(response.message())
            }
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
