package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemCpuFreqBinding
import com.androvine.deviceinfo.databinding.ItemNetDetailsBinding

class CpuCoreUsageListAdapter(private val cpuUsageData: MutableMap<Int, Long>) : RecyclerView.Adapter<CpuCoreUsageListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCpuFreqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cpuUsageData.entries.elementAt(position)
        holder.binding.coreName.text = "Core ${item.key}"
        holder.binding.coreFreq.text = (item.value.toLong() / 1000).toString() + " MHz"

    }



    override fun getItemCount(): Int {
        return cpuUsageData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newTemperatureData: Map<Int, Long>) {

        if (cpuUsageData.isEmpty()) {
            cpuUsageData.putAll(newTemperatureData)
            notifyDataSetChanged()
            return
        }

        for ((key, value) in newTemperatureData) {
            cpuUsageData[key] = value
            notifyItemChanged(getItemPosition(key))
        }
    }

    private fun getItemPosition(key: Int): Int {
        for ((index, entry) in cpuUsageData.entries.withIndex()) {
            if (entry.key == key) {
                return index
            }
        }
        return RecyclerView.NO_POSITION
    }


    class ViewHolder (val binding: ItemCpuFreqBinding) : RecyclerView.ViewHolder(binding.root)


}