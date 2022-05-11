package com.example.universityapp.presentation.university_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.databinding.FragmentUniversityListBinding
import com.example.universityapp.presentation.university_list.adapter.UniversityListAdapter
import com.example.universityapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UniversityListFragment : BindingFragment<FragmentUniversityListBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter = UniversityListAdapter()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val globalToken = requireActivity().intent.getStringExtra(Constant.GLOBAL_TOKEN)
        requestUniversityData(globalToken.toString())
    }

    private fun requestUniversityData(token: String) {
        lifecycleScope.launch {
            mainViewModel.getUniversityList(token)
            mainViewModel.universityListResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> binding.progressbar.isVisible = true
                    is Resource.Error -> {
                        if (result.message == "Token Expire") {
                            binding.errorImage.isVisible = false
                            binding.errorText.isVisible = false
                            requestGlobalToken()
                        } else {
                            binding.progressbar.isVisible = false
                            binding.errorImage.isVisible = true
                            binding.errorText.text = result.message
                        }
                    }
                    is Resource.Success -> {
                        binding.progressbar.isVisible = false
                        adapter.setData(result.data!!.data!!.filter {
                            it.url!!.isNotEmpty()
                        })
                        binding.recyclerview.adapter = adapter
                    }
                }
            }
        }
    }

    private fun requestGlobalToken() {
        lifecycleScope.launch {
            mainViewModel.getGlobalToken()
            mainViewModel.tokenResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> binding.progressbar.isVisible = true
                    is Resource.Error -> {
                        binding.progressbar.isVisible = false
                        binding.errorText.text = result.message
                        binding.errorImage.isVisible = true
                    }
                    is Resource.Success -> {
                        binding.progressbar.isVisible = false
                        val token = Constant.BEARER + result.data!!.access_token
                        requestUniversityData(token)
                    }
                }
            }
        }
    }
}

