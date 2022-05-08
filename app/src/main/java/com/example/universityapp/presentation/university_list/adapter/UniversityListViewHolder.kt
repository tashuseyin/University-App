package com.example.universityapp.presentation.university_list.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.universityapp.data.model.university.UniversityModel
import com.example.universityapp.databinding.UniversityItemCardBinding

class UniversityListViewHolder(private val binding: UniversityItemCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(universityModel: UniversityModel, onItemClickListener: ((String) -> Unit)?) {
        binding.apply {
            universityImage.load(universityModel.url) {
                crossfade(600)
            }
            universityTitle.text = universityModel.name

            binding.universityShortDetail.setOnClickListener {
                onItemClickListener?.invoke(universityModel.shortDetail)
            }
        }
    }
}