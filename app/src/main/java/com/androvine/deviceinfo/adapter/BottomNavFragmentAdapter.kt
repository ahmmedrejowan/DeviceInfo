package com.androvine.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androvine.deviceinfo.fragments.apps.AnalyzeFragment
import com.androvine.deviceinfo.fragments.apps.PermissionsFragment
import com.androvine.deviceinfo.fragments.apps.SystemAppsFragment
import com.androvine.deviceinfo.fragments.apps.UserAppsFragment
import com.androvine.deviceinfo.fragments.bottomNav.HomeFragment
import com.androvine.deviceinfo.fragments.bottomNav.ReportFragment
import com.androvine.deviceinfo.fragments.bottomNav.TestFragment
import com.androvine.deviceinfo.fragments.device.BatteryFragment
import com.androvine.deviceinfo.fragments.device.CameraFragment
import com.androvine.deviceinfo.fragments.device.CodecsFragment
import com.androvine.deviceinfo.fragments.device.ConnectionFragment
import com.androvine.deviceinfo.fragments.device.CpuFragment
import com.androvine.deviceinfo.fragments.device.DisplayFragment
import com.androvine.deviceinfo.fragments.device.FeatureFragment
import com.androvine.deviceinfo.fragments.device.GpuFragment
import com.androvine.deviceinfo.fragments.device.GsmFragment
import com.androvine.deviceinfo.fragments.device.MiscFragment
import com.androvine.deviceinfo.fragments.device.NetworkFragment
import com.androvine.deviceinfo.fragments.device.OsFragment
import com.androvine.deviceinfo.fragments.device.RamFragment
import com.androvine.deviceinfo.fragments.device.SensorFragment
import com.androvine.deviceinfo.fragments.device.StorageFragment
import com.androvine.deviceinfo.fragments.device.SystemFragment
import com.androvine.deviceinfo.fragments.device.ThermalFragment

class BottomNavFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return HomeFragment()
            }

            1 -> {
                return TestFragment()
            }

            2 -> {
                return ReportFragment()
            }


            else -> {
                return HomeFragment()
            }

        }
    }


}