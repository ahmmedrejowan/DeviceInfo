package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemNetDetailsBinding

class SensorListAdapter(private val temperatureData: MutableMap<String, String>) : RecyclerView.Adapter<SensorListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNetDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = temperatureData.entries.elementAt(position)
        holder.binding.valueID.text = item.key
        holder.binding.valueName.text = item.value

        if (position == temperatureData.size - 1) {
            holder.binding.divider.visibility = View.GONE
        }

    }



    override fun getItemCount(): Int {
        return temperatureData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newTemperatureData: Map<String, String>) {
        Log.e("SensorListAdapter", "updateList: ${newTemperatureData.size}")

        if (temperatureData.isEmpty()) {
            temperatureData.putAll(newTemperatureData)
            notifyDataSetChanged()
            return
        }

        for ((key, value) in newTemperatureData) {
            Log.e("SensorListAdapter", "updateList: $key $value")
            temperatureData[key] = value
            notifyItemChanged(getItemPosition(key))
        }
    }

    private fun getItemPosition(key: String): Int {
        for ((index, entry) in temperatureData.entries.withIndex()) {
            if (entry.key == key) {
                return index
            }
        }
        return RecyclerView.NO_POSITION
    }


    class ViewHolder (val binding: ItemNetDetailsBinding) : RecyclerView.ViewHolder(binding.root)


}