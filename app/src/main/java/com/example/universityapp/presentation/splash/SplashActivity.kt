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
import com.example.universityapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        requestGlobalToken()
    }


    private fun requestGlobalToken() {
        lifecycleScope.launch {
            mainViewModel.getGlobalToken()
            mainViewModel.tokenResponse.observe(this@SplashActivity) { result ->
                when (result) {
                    is Resource.Success -> {
                        val token = Constant.BEARER + result.data!!.access_token
                        Intent(
                            this@SplashActivity,
                            MainActivity::class.java
                        ).also {
                            startActivity(it)
                        }
                    }

                    is Resource.Error -> {
                        Intent(
                            this@SplashActivity,
                            MainActivity::class.java
                        ).also {
                            startActivity(it)
                        }
                    }
                }
            }
        }
    }

}