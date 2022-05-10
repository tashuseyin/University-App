package com.example.universityapp.presentation.university_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.university.UniversityData
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UniversityListViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _universityListState: MutableLiveData<Resource<UniversityData>> = MutableLiveData()
    val universityListState get() = _universityListState

    suspend fun getUniversityList(authorization: String) {
        _universityListState.value = Resource.Loading()
        try {
            val response = repository.getUniversityList(authorization)
            _universityListState.value = handleUniversityListResponse(response)
        } catch (e: Exception) {
            _universityListState.value =
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
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
}