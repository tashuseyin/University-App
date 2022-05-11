package com.example.universityapp.presentation.ui.university_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.universityapp.R
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.databinding.FragmentUniversityDetailBinding
import com.example.universityapp.presentation.ui.university_detail.adapter.SliderImageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UniversityDetailFragment : BindingFragment<FragmentUniversityDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityDetailBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val images = listOf(R.drawable.resim1, R.drawable.resim2, R.drawable.resim3)

        val adapter = SliderImageAdapter(images)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

        }.attach()
    }
}