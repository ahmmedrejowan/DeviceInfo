package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemAppsBinding
import com.androvine.deviceinfo.databinding.ItemNetDetailsBinding

class AppsListAdapter(private val listOfApps: MutableList<PackageInfo>) :
    RecyclerView.Adapter<AppsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemAppsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfApps[position]


    }


    override fun getItemCount(): Int {
        return listOfApps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<PackageInfo>) {
        listOfApps.clear()
        listOfApps.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemAppsBinding) : RecyclerView.ViewHolder(binding.root)


}