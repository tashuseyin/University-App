package com.example.universityapp.presentation.university_list

import android.app.Dialog
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
import com.example.universityapp.databinding.ShortDetailCardBinding
import com.example.universityapp.presentation.university_list.adapter.UniversityListAdapter
import com.example.universityapp.viewmodel.SharedViewModel
import com.example.universityapp.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UniversityListFragment : BindingFragment<FragmentUniversityListBinding>() {

    private lateinit var customListDialog: Dialog
    private lateinit var adapter: UniversityListAdapter
    private val universityListViewModel: UniversityListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUniversityListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val globalToken = requireActivity().intent.getStringExtra(Constant.GLOBAL_TOKEN)
        observeUniversityData(globalToken.toString())

        adapter = UniversityListAdapter()
        setListener()
    }

    private fun setListener() {
        adapter.onItemClickListener = { shortDetail ->
            customShortDetailDialog(shortDetail)
        }

    }

    private fun customShortDetailDialog(shortDetail: String) {
        val customDialogBinding: ShortDetailCardBinding =
            ShortDetailCardBinding.inflate(layoutInflater)
        customListDialog.setContentView(customDialogBinding.root)
        customDialogBinding.universityCardShortDetail.text = shortDetail
        customListDialog.show()
    }

    private fun observeUniversityData(authorization: String) {
        lifecycleScope.launch {
            universityListViewModel.getUniversityList(authorization)
            universityListViewModel.universityListState.observe(viewLifecycleOwner) { universityDataResult ->
                when (universityDataResult) {
                    is Resource.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.progressbar.isVisible = false
                        binding.errorTextView.text = universityDataResult.message
                        if (universityDataResult.data!!.status == 401) {
                            print("a")
                            getGlobalToken()
                        }
                    }
                    is Resource.Success -> {
                        binding.progressbar.isVisible = false
                        if (universityDataResult.data!!.data.isNotEmpty()) {
                            adapter.setData(universityDataResult.data.data)
                        }
                        binding.recyclerview.adapter = adapter
                    }
                }
            }
        }
    }

    private fun getGlobalToken() {
        lifecycleScope.launch {
            sharedViewModel.getGlobalToken()
            sharedViewModel.tokenState.observe(viewLifecycleOwner) { tokenData ->
                when (tokenData) {
                    is Resource.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.errorImageView.isVisible = true
                        binding.errorTextView.text = tokenData.message
                    }
                    is Resource.Success -> {
                        observeUniversityData("Bearer" + tokenData.data!!.access_token)
                    }
                }

            }
        }
    }

}

