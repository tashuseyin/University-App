package com.example.universityapp.presentation.ui.university_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.databinding.SliderImageContainerBinding

class SliderImageAdapter : RecyclerView.Adapter<SliderImageAdapter.SliderImageViewHolder>() {
    private var imageList = emptyList<Int>()

    inner class SliderImageViewHolder(private val binding: SliderImageContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int) {
            binding.universityImage.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderImageViewHolder {
        val binding =
            SliderImageContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount() = imageList.size

    fun setData(newData: List<Int>) {
        this.imageList = newData
    }
}