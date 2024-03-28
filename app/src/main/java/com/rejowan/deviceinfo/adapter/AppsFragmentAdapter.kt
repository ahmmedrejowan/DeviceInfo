package com.rejowan.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rejowan.deviceinfo.fragments.apps.SystemAppsFragment
import com.rejowan.deviceinfo.fragments.apps.UserAppsFragment

class AppsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return UserAppsFragment()
            }

            1 -> {
                return SystemAppsFragment()
            }

            else -> {
                return UserAppsFragment()
            }

        }
    }


}