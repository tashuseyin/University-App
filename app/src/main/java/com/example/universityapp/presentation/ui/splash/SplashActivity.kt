package com.example.universityapp.presentation.ui.splash

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
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        requestGlobalToken()
    }


    private fun requestGlobalToken() {
        lifecycleScope.launch {
            sharedViewModel.getGlobalToken(sharedViewModel.applyGlobalTokenQueries())
            sharedViewModel.tokenResponse.observe(this@SplashActivity) { response ->
                if (response is Resource.Success) {
                    val globalToken = Constant.BEARER + response.data!!.access_token
                    sharedViewModel.saveGlobalToken(globalToken)
                }
            }
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

}