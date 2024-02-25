package com.androvine.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androvine.deviceinfo.fragments.bottomNav.HomeFragment
import com.androvine.deviceinfo.fragments.bottomNav.MonitorFragment
import com.androvine.deviceinfo.fragments.bottomNav.TestFragment

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
                return MonitorFragment()
            }

            2 -> {
                return TestFragment()
            }


            else -> {
                return HomeFragment()
            }

        }
    }


}