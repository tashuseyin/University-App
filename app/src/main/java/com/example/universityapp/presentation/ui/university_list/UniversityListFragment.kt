package com.example.universityapp.presentation.ui.university_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.databinding.FragmentUniversityListBinding
import com.example.universityapp.presentation.MainActivity
import com.example.universityapp.presentation.ui.university_list.adapter.UniversityListAdapter
import com.example.universityapp.viewmodel.SharedViewModel
import com.example.universityapp.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UniversityListFragment : BindingFragment<FragmentUniversityListBinding>() {
    private val universityListViewModel: UniversityListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter = UniversityListAdapter()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestApi()
        navigateFragment()
    }

    private fun navigateFragment() {
        adapter.onItemClickListener = { uniId, uniTitle ->
            (activity as MainActivity).hideBottomNavigation()
            sharedViewModel.readLoginStatus.observe(viewLifecycleOwner) { loginStatus ->
                if (!loginStatus) {
                    findNavController().navigate(
                        UniversityListFragmentDirections.actionUniversityListToLoginFragment(
                            uniTitle, uniId
                        )
                    )
                } else {
                    findNavController().navigate(
                        UniversityListFragmentDirections.actionUniversityListToUniversityDetail(
                            uniTitle, uniId
                        )
                    )
                }
            }
        }
    }

    private fun requestApi() {
        lifecycleScope.launch {
            sharedViewModel.readGlobalToken.observe(viewLifecycleOwner) { token ->
                requestUniversityData(token)
            }
        }
    }


    private fun requestUniversityData(token: String) {
        lifecycleScope.launch {
            universityListViewModel.getUniversityList(token)
            universityListViewModel.universityListResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.errorImage.isVisible = false
                        if (result.message == "Token Expire") {
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
            sharedViewModel.getGlobalToken(sharedViewModel.applyGlobalTokenQueries())
            sharedViewModel.tokenResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressbar.isVisible = true
                    }
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavigation()
    }
}

