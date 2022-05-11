package com.example.universityapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.data_store.DataStoreRepository
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val repository: UniversityRepository
) : AndroidViewModel(application) {


    val readGlobalToken = dataStoreRepository.readToken.asLiveData()

    private fun saveGlobalToken(globalToken: String) = viewModelScope.launch {
        dataStoreRepository.saveToken(globalToken)
    }


    private val _tokenResponse: MutableLiveData<Resource<TokenData>> = MutableLiveData()
    val tokenResponse get() = _tokenResponse

    suspend fun getGlobalToken() {
        _tokenResponse.value = Resource.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getGlobalToken(applyTokenQueries())
                _tokenResponse.value = Resource.Success(response.body()!!)
                val globalToken = Constant.BEARER + response.body()!!.access_token
                saveGlobalToken(globalToken)
            } catch (e: Exception) {
                _tokenResponse.value =
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        } else {
            _tokenResponse.value = Resource.Error("No Internet Connection.")
        }
    }

    private fun applyTokenQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constant.QUERY_GRANT_TYPE] = Constant.GRANT_TYPE_VALUE
        queries[Constant.QUERY_CLIENT_ID] = Constant.CLIENT_ID_VALUE
        queries[Constant.QUERY_CLIENT_SECRET] = Constant.CLIENT_SECRET_VALUE

        return queries
    }


    private val _universityListResponse: MutableLiveData<Resource<UniversityData>> =
        MutableLiveData()
    val universityListResponse get() = _universityListResponse

    suspend fun getUniversityList(authorization: String) {
        _universityListResponse.value = Resource.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getUniversityList(authorization)
                _universityListResponse.value = handleUniversityListResponse(response)
            } catch (e: Exception) {
                _universityListResponse.value =
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        } else {
            _universityListResponse.value = Resource.Error("No internet connection.")
        }
    }

    private fun handleUniversityListResponse(response: Response<UniversityData>): Resource<UniversityData> {
        return when {
            response.message().toString().contains("timeout") -> {
                Resource.Error("Timeout")
            }
            response.code() == 401 -> {
                Resource.Error("Token Expire")
            }
            response.isSuccessful -> {
                val universityData = response.body()
                Resource.Success(universityData!!)
            }
            else -> {
                Resource.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
