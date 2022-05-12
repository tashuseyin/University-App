package com.example.universityapp.presentation.ui.university_detail.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.data.model.university_detail.MajorDetail
import com.example.universityapp.databinding.UniversityDetailItemCardBinding

class UniversitySectionViewHolder(private val binding: UniversityDetailItemCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(majorDetail: MajorDetail, size: Int) {
        binding.majorDetailName.text = majorDetail.name
        binding.majorDetailSchoolType.text = majorDetail.schoolType

        if (adapterPosition == size - 1){
            binding.line.isVisible = false
        }
    }
}