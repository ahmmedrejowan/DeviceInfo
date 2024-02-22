package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemSensorBinding

class SensorListAdapter(private val listOfDetails: MutableList<Sensor>, private val listener: OnSensorItemClicked) : RecyclerView.Adapter<SensorListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSensorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfDetails[position]

        holder.binding.sensorName.text = item.name
        holder.binding.sensorModel.text = item.vendor

        if (position == listOfDetails.size - 1) {
            holder.binding.divider.visibility = View.GONE
        }

        holder.binding.sensorlayout.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.binding.arrow.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.binding.sensorName.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.binding.sensorModel.setOnClickListener {
            listener.onItemClick(position)
        }

    }



    override fun getItemCount(): Int {
        return listOfDetails.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Sensor>){
        listOfDetails.clear()
        listOfDetails.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder (val binding: ItemSensorBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnSensorItemClicked {
        fun onItemClick(position: Int)
    }

}