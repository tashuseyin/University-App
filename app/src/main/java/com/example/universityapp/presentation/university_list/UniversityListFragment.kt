package com.example.universityapp.presentation.university_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Resource
import com.example.universityapp.databinding.FragmentUniversityListBinding
import com.example.universityapp.viewmodel.SharedViewModel
import com.example.universityapp.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UniversityListFragment : BindingFragment<FragmentUniversityListBinding>() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private val universityListViewModel: UniversityListViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            sharedViewModel.getGlobalCoins()
            sharedViewModel.tokenState.observe(viewLifecycleOwner) { tokenResult ->
                when (tokenResult) {
                    is Resource.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.errorTextView.text = tokenResult.message
                    }
                    is Resource.Success -> {
                        val data = tokenResult.data
                        print(data)
                    }
                }

            }
        }
    }

}