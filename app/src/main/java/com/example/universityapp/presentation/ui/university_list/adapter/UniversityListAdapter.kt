package com.example.universityapp.presentation.ui.university_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.data.model.university.UniversityItem
import com.example.universityapp.databinding.UniversityItemCardBinding
import com.example.universityapp.util.UniversitiesDiffUtil

class UniversityListAdapter : RecyclerView.Adapter<UniversityListViewHolder>() {
    private var universityList = emptyList<UniversityItem>()
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityListViewHolder {
        val binding =
            UniversityItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityListViewHolder, position: Int) {
        holder.bind(universityList[position], onItemClickListener)
    }

    override fun getItemCount() = universityList.size

    fun setData(newData: List<UniversityItem>) {
        val universityDiffUtil = UniversitiesDiffUtil(universityList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(universityDiffUtil)
        universityList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}