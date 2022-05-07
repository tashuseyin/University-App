package com.example.universityapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.university.UniversityModel
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UniversityListViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _universityListState: MutableLiveData<Resource<List<UniversityModel>>> =
        MutableLiveData()
    val universityListState get() = _universityListState

    suspend fun getUniversityList(authorization: String) {
        try {
            val response = repository.getUniversityList(authorization)
            _universityListState.value = Resource.Success(response.data)
        } catch (e: Exception) {
            _universityListState.value =
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}