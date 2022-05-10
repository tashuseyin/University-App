package com.example.universityapp.presentation.university_list.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.universityapp.data.model.university.UniversityItem
import com.example.universityapp.databinding.UniversityItemCardBinding

class UniversityListViewHolder(private val binding: UniversityItemCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(universityItem: UniversityItem) {
        binding.universityImage.load(universityItem.url) {
            crossfade(600)
        }
        binding.universityTitle.text = universityItem.name
        binding.universityShortDetail.text = universityItem.shortDetail
    }
}