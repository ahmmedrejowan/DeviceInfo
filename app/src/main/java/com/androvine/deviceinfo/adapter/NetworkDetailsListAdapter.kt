package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemNetDetailsBinding

class NetworkDetailsListAdapter(private val listOfDetails: MutableList<Pair<String, String>>) : RecyclerView.Adapter<NetworkDetailsListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNetDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfDetails[position]

        holder.binding.valueID.text = item.first
        holder.binding.valueName.text = item.second

        if (position == listOfDetails.size - 1) {
            holder.binding.divider.visibility = View.GONE
        }

    }



    override fun getItemCount(): Int {
        return listOfDetails.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Pair<String, String>>){
        listOfDetails.clear()
        listOfDetails.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder (val binding: ItemNetDetailsBinding) : RecyclerView.ViewHolder(binding.root)


}