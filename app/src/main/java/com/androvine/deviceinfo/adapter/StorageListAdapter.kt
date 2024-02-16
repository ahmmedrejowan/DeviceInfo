package com.androvine.deviceinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemStorageBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils
import com.androvine.deviceinfo.detailsMVVM.dataClass.MiniStorageModel

class StorageListAdapter(private val listOfStorage: MutableList<MiniStorageModel>) : RecyclerView.Adapter<StorageListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfStorage[position]
        holder.binding.romName.text = item.storageName
        holder.binding.path.text = item.storagePath
        holder.binding.totalSize.text = DeviceDetailsUtils.bytesToHuman(item.totalSize)
        holder.binding.usedSize.text = DeviceDetailsUtils.bytesToHuman(item.usedSize)
        holder.binding.freeSize.text = DeviceDetailsUtils.bytesToHuman(item.freeSize)

        val usedPercentage = (item.usedSize * 100) / item.totalSize
        holder.binding.progressBar.progress = usedPercentage.toInt()

    }



    override fun getItemCount(): Int {
        return listOfStorage.size
    }

    fun updateList(newList: List<MiniStorageModel>){
        listOfStorage.clear()
        listOfStorage.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder (val binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root)


}