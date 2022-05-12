package com.example.universityapp.presentation.ui.university_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.data.model.university_detail.MajorDetail
import com.example.universityapp.databinding.UniversityDetailItemCardBinding

class UniversitySectionAdapter : RecyclerView.Adapter<UniversitySectionViewHolder>() {
    private var majorDetailList = emptyList<MajorDetail>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversitySectionViewHolder {
        val binding = UniversityDetailItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UniversitySectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversitySectionViewHolder, position: Int) {
        holder.bind(majorDetailList[position], majorDetailList.size)
    }

    override fun getItemCount(): Int {
        return majorDetailList.size
    }

    fun setData(newMajorDetailList: List<MajorDetail>) {
        this.majorDetailList = newMajorDetailList
    }
}