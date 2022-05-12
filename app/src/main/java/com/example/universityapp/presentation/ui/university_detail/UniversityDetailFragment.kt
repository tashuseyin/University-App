package com.example.universityapp.presentation.ui.university_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.university_detail.Data
import com.example.universityapp.data.model.university_detail.Image
import com.example.universityapp.databinding.FragmentUniversityDetailBinding
import com.example.universityapp.presentation.ui.university_detail.adapter.SliderImageAdapter
import com.example.universityapp.presentation.ui.university_detail.adapter.UniversitySectionAdapter
import com.example.universityapp.viewmodel.SharedViewModel
import com.example.universityapp.viewmodel.UniversityDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UniversityDetailFragment : BindingFragment<FragmentUniversityDetailBinding>() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private val universityDetailViewModel: UniversityDetailViewModel by viewModels()
    private val args: UniversityDetailFragmentArgs by navArgs()
    private val adapter = UniversitySectionAdapter()
    private val sliderAdapter = SliderImageAdapter()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityDetailBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeLoginStatus()
        requestApi()
    }

    private fun sliderImage(imageList: List<Image>) {
        sliderAdapter.setData(imageList)
        binding.viewPager.adapter = sliderAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }

    private fun changeLoginStatus() {
        lifecycleScope.launch {
            sharedViewModel.saveLoginStatus(true)
        }
    }


    private fun requestApi() {
        lifecycleScope.launch {
            sharedViewModel.readLoginToken.observe(viewLifecycleOwner) { token ->
                requestUniversityData(args.uniId, token)
            }
        }
    }


    private fun requestUniversityData(uniId: Int, token: String) {
        lifecycleScope.launch {
            universityDetailViewModel.getUniversityDetail(uniId, token)
            universityDetailViewModel.universityDetailResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Error -> {
                        if (result.message == "Token Expire") {
                            binding.errorImage.isVisible = false
                            binding.errorText.isVisible = false
                            readUserInfo()
                        } else {
                            binding.progressBar.isVisible = false
                            binding.errorImage.isVisible = true
                            binding.errorText.text = result.message
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        binding.constraint.isVisible = true
                        result.data?.data?.let { data ->
                            setUniversityDetailView(data)
                            data.images?.let { sliderImage(it) }
                            adapter.setData(data.majorDetail!!)
                            binding.recyclerview.adapter = adapter
                        }
                    }
                }
            }
        }
    }

    private fun readUserInfo() {
        lifecycleScope.launch {
            sharedViewModel.readUserInfo.observe(viewLifecycleOwner) {
                requestGlobalToken(it.username, it.password)
            }
        }
    }

    private fun requestGlobalToken(username: String, password: String) {
        lifecycleScope.launch {
            sharedViewModel.getGlobalToken(
                sharedViewModel.applyLoginTokenQueries(
                    username,
                    password
                )
            )
            sharedViewModel.tokenResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.progressBar.isVisible = false
                        binding.errorText.text = result.message
                        binding.errorImage.isVisible = true
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        binding.constraint.isVisible = true
                        val token = Constant.BEARER + result.data!!.access_token
                        requestUniversityData(args.uniId, token)
                    }
                }
            }
        }
    }

    private fun setUniversityDetailView(universityDetail: Data) {
        binding.apply {
            universityName.text = universityDetail.name
            universityType.text = universityDetail.type
            universityLongDetail.text = universityDetail.detail
        }
    }
}