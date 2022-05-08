package com.example.universityapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.universityapp.R
import com.example.universityapp.common.Constant
import com.example.universityapp.common.Resource
import com.example.universityapp.presentation.MainActivity
import com.example.universityapp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            delay(1500)
            observeGlobalToken()
        }
    }

    private fun observeGlobalToken() {
        lifecycleScope.launch {
            sharedViewModel.tokenState.observe(this@SplashActivity) { tokenResult ->
                when (tokenResult) {
                    is Resource.Success -> {
                        val globalToken = "Bearer" + tokenResult.data!!.access_token
                        Intent(this@SplashActivity, MainActivity::class.java).also {
                            it.putExtra(Constant.GLOBAL_TOKEN, globalToken)
                            startActivity(it)
                        }
                    }
                }
            }
        }
    }
}