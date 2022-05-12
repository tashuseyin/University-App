package com.example.universityapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.university_detail.UniversityDetailData
import com.example.universityapp.data.repository.UniversityRepository
import com.example.universityapp.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UniversityDetailViewModel @Inject constructor(
    application: Application,
    private val repository: UniversityRepository
) : AndroidViewModel(application) {

    private val _universityDetailResponse: MutableLiveData<Resource<UniversityDetailData>> =
        MutableLiveData()
    val universityDetailResponse get() = _universityDetailResponse

    suspend fun getUniversityDetail(uniId: Int, authorization: String) {
        _universityDetailResponse.value = Resource.Loading()
        if (Utils.hasInternetConnection(application = getApplication())) {
            try {
                val response = repository.getUniversityDetail(uniId, authorization)
                _universityDetailResponse.value = handleUniversityDetailResponse(response)
            } catch (e: Exception) {
                _universityDetailResponse.value =
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        } else {
            _universityDetailResponse.value = Resource.Error("No internet connection.")
        }
    }

    private fun handleUniversityDetailResponse(response: Response<UniversityDetailData>): Resource<UniversityDetailData> {
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
}