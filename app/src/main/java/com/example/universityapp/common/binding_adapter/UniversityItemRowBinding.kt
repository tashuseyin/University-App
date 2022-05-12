package com.example.universityapp.common.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.universityapp.R

class UniversityItemRowBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(300)
                error(R.drawable.ic_error_palceholder)
            }
        }
    }
}