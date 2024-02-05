package com.androvine.deviceinfo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.adapter.DeviceFragmentAdapter
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsFragmentAdapter
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.androvine.deviceinfo.fragments.bottomNav.HomeFragment
import com.androvine.deviceinfo.fragments.bottomNav.ReportFragment
import com.androvine.deviceinfo.fragments.bottomNav.TestFragment
import com.google.android.material.tabs.TabLayoutMediator

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupBottomNavigation()

        setUpDeviceTabs()

        setUpAppsTabs()

    }


    private fun setupBottomNavigation() {

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    setUpFragment(HomeFragment())
                    showHide(navContainer = true, deviceTab = false, appsTab = false)
                }

                R.id.nav_device -> {
                    showHide(navContainer = false, deviceTab = true, appsTab = false)

                }

                R.id.nav_test -> {
                    setUpFragment(TestFragment())
                    showHide(navContainer = true, deviceTab = false, appsTab = false)

                }

                R.id.nav_apps -> {
                    showHide(navContainer = false, deviceTab = false, appsTab = true)

                }

                R.id.nav_report -> {
                    setUpFragment(ReportFragment())
                    showHide(navContainer = true, deviceTab = false, appsTab = false)

                }
            }
            true
        }

        binding.bottomNavView.selectedItemId = R.id.nav_home


    }

    private fun showHide(navContainer: Boolean, deviceTab: Boolean, appsTab: Boolean) {

        binding.fragmentContainerView.visibility = if (navContainer) View.VISIBLE else View.GONE
        binding.viewPagerDevice.visibility = if (deviceTab) View.VISIBLE else View.GONE
        binding.viewPagerApps.visibility = if (appsTab) View.VISIBLE else View.GONE

        binding.tabLayoutApps.visibility = if (appsTab) View.VISIBLE else View.GONE
        binding.tabLayoutDevice.visibility = if (deviceTab) View.VISIBLE else View.GONE

    }


    private fun setUpDeviceTabs() {

        binding.tabLayoutDevice.visibility = View.GONE

        val tabList = listOf("CPU", "GPU", "OS", "System", "Display", "RAM", "Storage", "Battery", "Camera", "Connection", "Network", "GSM", "Sensor", "Thermal", "Codec", "Feature", "Misc")

        binding.viewPagerDevice.adapter = DeviceFragmentAdapter(supportFragmentManager,lifecycle)

        TabLayoutMediator(binding.tabLayoutDevice, binding.viewPagerDevice) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }

    private fun setUpAppsTabs() {

        binding.tabLayoutApps.visibility = View.GONE

        val tabList = listOf("User", "System", "Permission", "Analytics")

        binding.viewPagerApps.adapter = AppsFragmentAdapter(supportFragmentManager,lifecycle)

        TabLayoutMediator(binding.tabLayoutApps, binding.viewPagerApps) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }

    private fun setUpFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }


}