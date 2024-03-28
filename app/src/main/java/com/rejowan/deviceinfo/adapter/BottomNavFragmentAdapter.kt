package com.rejowan.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rejowan.deviceinfo.fragments.bottomNav.HomeFragment
import com.rejowan.deviceinfo.fragments.bottomNav.MonitorFragment
import com.rejowan.deviceinfo.fragments.bottomNav.TestFragment

class BottomNavFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }

            1 -> {
                MonitorFragment()
            }

            2 -> {
                TestFragment()
            }


            else -> {
                HomeFragment()
            }

        }
    }


}