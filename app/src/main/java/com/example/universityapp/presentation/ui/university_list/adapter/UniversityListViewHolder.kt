package com.example.universityapp.presentation.ui.university_list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.data.model.university.UniversityItem
import com.example.universityapp.databinding.UniversityItemCardBinding

class UniversityListViewHolder(private val binding: UniversityItemCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(universityItem: UniversityItem) {
        binding.result = universityItem
        binding.executePendingBindings()
    }
}