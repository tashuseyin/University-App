package com.example.universityapp.baseviewmodel

import androidx.lifecycle.ViewModel
import com.example.universityapp.data.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private fun getGlobalCoins() {


    }
}