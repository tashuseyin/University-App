package com.example.universityapp.presentation.ui.login_page

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.universityapp.R
import com.example.universityapp.common.BindingFragment
import com.example.universityapp.common.Resource
import com.example.universityapp.databinding.FragmentLoginBinding
import com.example.universityapp.util.Utils.showErrorSnackBar
import com.example.universityapp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }


    private fun setListener() {
        binding.apply {
            loginButton.setOnClickListener {
                requestLoginSubmit()
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.username.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(requireActivity(), getString(R.string.error_message_email), true)
                false
            }
            TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    requireActivity(),
                    getString(R.string.error_message_password),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }


    private fun requestLoginSubmit() {
        if (validateLoginDetails()) {
            val username = binding.username.text.toString().trim { it <= ' ' }
            val password = binding.password.text.toString().trim { it <= ' ' }
            lifecycleScope.launch {
                sharedViewModel.getGlobalToken(
                    sharedViewModel.applyLoginTokenQueries(
                        username,
                        password
                    )
                )
                sharedViewModel.tokenResponse.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Loading -> binding.progressbar.isVisible = true
                        is Resource.Error -> {
                            binding.progressbar.isVisible = false
                            showErrorSnackBar(requireActivity(), result.message.toString(), true)
                        }
                        is Resource.Success -> {
                            binding.progressbar.isVisible = false
                            sharedViewModel.saveUserInfo(username, password)
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToUniversityDetail(
                                    args.uniTitle,
                                    args.uniId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}