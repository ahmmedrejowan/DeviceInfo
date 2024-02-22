package com.androvine.deviceinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androvine.deviceinfo.fragments.apps.AnalyzeFragment
import com.androvine.deviceinfo.fragments.apps.PermissionsFragment
import com.androvine.deviceinfo.fragments.apps.SystemAppsFragment
import com.androvine.deviceinfo.fragments.apps.UserAppsFragment

class AppsFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return UserAppsFragment()
            }

            1 -> {
                return SystemAppsFragment()
            }

            2 -> {
                return PermissionsFragment()
            }

            3 -> {
                return AnalyzeFragment()
            }


            else -> {
                return UserAppsFragment()
            }

        }
    }


}