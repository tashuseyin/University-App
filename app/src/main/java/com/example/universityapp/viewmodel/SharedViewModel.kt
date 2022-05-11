package com.example.universityapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.data_store.DataStoreRepository
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.repository.UniversityRepository
import com.example.universityapp.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class SharedViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val repository: UniversityRepository
) : AndroidViewModel(application) {

    val readGlobalToken = dataStoreRepository.readToken.asLiveData()

    fun saveGlobalToken(globalToken: String) = viewModelScope.launch {
        dataStoreRepository.saveToken(globalToken)
    }

    val readLoginStatus = dataStoreRepository.readLoginStatus.asLiveData()

    fun saveLoginStatus(loginStatus: Boolean) = viewModelScope.launch {
        dataStoreRepository.saveLoginStatus(loginStatus)
    }

    val readUserInfo = dataStoreRepository.readUserInfo

    fun saveUserInfo(username: String, password: String) = viewModelScope.launch {
        dataStoreRepository.saveUserInformation(username, password)
    }

    private val _tokenResponse: MutableLiveData<Resource<TokenData>> = MutableLiveData()
    val tokenResponse get() = _tokenResponse

    suspend fun getGlobalToken(queries: Map<String, String>) {
        _tokenResponse.value = Resource.Loading()
        if (Utils.hasInternetConnection(application = getApplication())) {
            try {
                val response = repository.getGlobalToken(queries)
                _tokenResponse.value = Resource.Success(response.body()!!)
            } catch (e: Exception) {
                _tokenResponse.value =
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        } else {
            _tokenResponse.value = Resource.Error("No Internet Connection.")
        }
    }

    fun applyGlobalTokenQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constant.QUERY_GRANT_TYPE] = Constant.GRANT_TYPE_GLOBAL_VALUE
        queries[Constant.QUERY_CLIENT_ID] = Constant.CLIENT_ID_VALUE
        queries[Constant.QUERY_CLIENT_SECRET] = Constant.CLIENT_SECRET_VALUE

        return queries
    }


    fun applyLoginTokenQueries(password: String, username: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constant.QUERY_GRANT_TYPE] = Constant.GRANT_TYPE_LOGIN_VALUE
        queries[Constant.QUERY_CLIENT_ID] = Constant.CLIENT_ID_VALUE
        queries[Constant.QUERY_CLIENT_SECRET] = Constant.CLIENT_SECRET_VALUE
        queries[Constant.QUERY_PASSWORD] = password
        queries[Constant.QUERY_USERNAME] = username

        return queries
    }

}