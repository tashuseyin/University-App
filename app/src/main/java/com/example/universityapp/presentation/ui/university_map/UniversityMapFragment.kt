package com.example.universityapp.presentation.ui.university_map

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.databinding.FragmentUniverstiyMapBinding


class UniversityMapFragment : BindingFragment<FragmentUniverstiyMapBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniverstiyMapBinding::inflate

}