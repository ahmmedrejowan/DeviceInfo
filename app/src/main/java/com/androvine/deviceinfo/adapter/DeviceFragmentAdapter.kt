package com.androvine.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androvine.deviceinfo.fragments.device.BatteryFragment
import com.androvine.deviceinfo.fragments.device.CameraFragment
import com.androvine.deviceinfo.fragments.device.CodecsFragment
import com.androvine.deviceinfo.fragments.device.ConnectionFragment
import com.androvine.deviceinfo.fragments.device.CpuFragment
import com.androvine.deviceinfo.fragments.device.DisplayFragment
import com.androvine.deviceinfo.fragments.device.GpuFragment
import com.androvine.deviceinfo.fragments.device.GsmFragment
import com.androvine.deviceinfo.fragments.device.NetworkFragment
import com.androvine.deviceinfo.fragments.device.OsFragment
import com.androvine.deviceinfo.fragments.device.RamFragment
import com.androvine.deviceinfo.fragments.device.SensorFragment
import com.androvine.deviceinfo.fragments.device.StorageFragment
import com.androvine.deviceinfo.fragments.device.SystemFragment
import com.androvine.deviceinfo.fragments.device.ThermalFragment

class DeviceFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 17
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return CpuFragment()
            }

            1 -> {
                return GpuFragment()
            }

            2 -> {
                return OsFragment()
            }

            3 -> {
                return SystemFragment()
            }

            4 -> {
                return DisplayFragment()
            }

            5 -> {
                return RamFragment()
            }

            6 -> {
                return StorageFragment()
            }

            7 -> {
                return BatteryFragment()
            }

            8 -> {
                return CameraFragment()
            }

            9 -> {
                return ConnectionFragment()
            }

            10 -> {
                return NetworkFragment()
            }

            11 -> {
                return GsmFragment()
            }

            12 -> {
                return SensorFragment()
            }

            13 -> {
                return ThermalFragment()
            }

            14 -> {
                return CodecsFragment()
            }


            else -> {
                return CpuFragment()
            }

        }
    }


}