package com.androvine.deviceinfo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androvine.deviceinfo.databinding.ItemAppsBinding
import com.androvine.deviceinfo.ui.apps.AppDetails
import com.bumptech.glide.Glide
import java.io.File

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

        val name = item.applicationInfo.loadLabel(holder.itemView.context.packageManager).toString()
        val packageName = item.packageName
        val version = item.versionName
        val targetApi = item.applicationInfo.targetSdkVersion.toString()
        val appIcon = item.applicationInfo.loadIcon(holder.itemView.context.packageManager)
        val appSize = File(item.applicationInfo.sourceDir).length() / (1024f * 1024f)

        holder.binding.appName.text = name
        holder.binding.appPackage.text = packageName
        holder.binding.appVersionName.text = version
        holder.binding.appTargetAPI.text = "API $targetApi"
        Glide.with(holder.binding.root.context).load(appIcon).into(holder.binding.appIcon)
        holder.binding.appSize.text = String.format("%.2f", appSize) + " MB"

        holder.binding.arrow.setOnClickListener {
            openAppDetails(holder.binding.root.context,item)
        }

        holder.binding.appName.setOnClickListener {
            openAppDetails(holder.binding.root.context,item)
        }

        holder.binding.appPackage.setOnClickListener {
            openAppDetails(holder.binding.root.context,item)
        }

        holder.binding.parentLayout.setOnClickListener {
            openAppDetails(holder.binding.root.context,item)
        }

    }

    private fun openAppDetails(context: Context, item: PackageInfo) {
        val intent = Intent(context, AppDetails::class.java)
        intent.putExtra("packageName", item.packageName)
        context.startActivity(intent)

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