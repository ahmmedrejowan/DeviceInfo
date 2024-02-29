package com.androvine.deviceinfo.fragments.apps

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentSystemAppsBinding
import com.androvine.deviceinfo.databinding.FragmentUserAppsBinding


class SystemAppsFragment : Fragment() {

    private val binding by lazy { FragmentSystemAppsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSystemApps()


    }

    private fun getSystemApps() {
        val pm = requireActivity().packageManager
        val listOfApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            pm.getInstalledPackages(0)
        }
        val systemApps = listOfApps.filter { it.applicationInfo.flags and 1 != 0 }
        Log.e("SystemApps", systemApps.size.toString())

    }
}