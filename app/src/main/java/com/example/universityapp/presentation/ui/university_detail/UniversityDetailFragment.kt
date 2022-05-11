package com.example.universityapp.presentation.ui.university_detail

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.databinding.FragmentUniversityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UniversityDetailFragment : BindingFragment<FragmentUniversityDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityDetailBinding::inflate

}