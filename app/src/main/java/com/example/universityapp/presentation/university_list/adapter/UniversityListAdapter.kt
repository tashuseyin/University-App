package com.example.universityapp.presentation.university_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.data.model.university.UniversityModel
import com.example.universityapp.databinding.UniversityItemCardBinding

class UniversityListAdapter : RecyclerView.Adapter<UniversityListViewHolder>() {

    private var universityList = emptyList<UniversityModel>()
    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityListViewHolder {
        val binding =
            UniversityItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityListViewHolder, position: Int) {
        holder.bind(universityList[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return universityList.size
    }

    fun setData(newData: List<UniversityModel>) {
        this.universityList = newData
    }

}