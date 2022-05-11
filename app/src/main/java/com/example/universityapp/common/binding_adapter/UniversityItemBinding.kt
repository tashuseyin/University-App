package com.example.universityapp.common.binding_adapter

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.universityapp.common.Resource
import com.example.universityapp.data.model.token.TokenData
import com.example.universityapp.data.model.university.UniversityData

class UniversityItemBinding {
    companion object {

        @BindingAdapter(
            "readUniversityListApiResponse",
            "readRefreshGlobalTokenApiResponse",
            requireAll = false
        )
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            universityListApiResponse: Resource<UniversityData>?,
            globalTokenApiResponse: Resource<TokenData>?
        ) {
            when (view) {
                is ProgressBar -> {
                    view.isVisible =
                        universityListApiResponse is Resource.Loading || globalTokenApiResponse is Resource.Loading
                }

            }
        }

    }
}