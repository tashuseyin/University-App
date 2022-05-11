package com.example.universityapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.universityapp.data.repository.UniversityRepository
import javax.inject.Inject

class UniversityDetailViewModel @Inject constructor(
    application: Application,
    private val repository: UniversityRepository
) : AndroidViewModel(application) {



}