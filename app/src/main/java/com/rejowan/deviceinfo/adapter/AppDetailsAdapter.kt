package com.rejowan.deviceinfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rejowan.deviceinfo.databinding.ItemAppsNormalBinding

class AppDetailsAdapter(private val listOfStrings: MutableList<String>) : RecyclerView.Adapter<AppDetailsAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAppsNormalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfStrings[position]
        holder.binding.text.text = item

    }



    override fun getItemCount(): Int {
        return listOfStrings.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<String>){
        listOfStrings.clear()
        listOfStrings.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder (val binding: ItemAppsNormalBinding) : RecyclerView.ViewHolder(binding.root)


}