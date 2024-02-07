package com.androvine.deviceinfo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsFragmentAdapter
import com.androvine.deviceinfo.adapter.BottomNavFragmentAdapter
import com.androvine.deviceinfo.adapter.DeviceFragmentAdapter
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupBottomNavigation()

        setUpDeviceTabs()

        setUpAppsTabs()

        setOnclickListeners()

        val cpuDatabaseHelper = CpuDatabaseHelper(this)
        lifecycleScope.launch {
            // dispatchers.io
            withContext(Dispatchers.IO) {
                cpuDatabaseHelper.copyDatabaseFromAssets(this@Home)
            }
        }

        val cpu = cpuDatabaseHelper.getCpuDataByModel("SM7325")
        Log.e("TAG", "CPU: " + cpu?.model + " " + cpu?.name + " " + cpu?.fab + " " + cpu?.gpu)

        val deviceDatabaseHelper = DeviceDatabaseHelper(this)
        lifecycleScope.launch {
            // dispatchers.io
            withContext(Dispatchers.IO) {
                deviceDatabaseHelper.copyDatabaseFromAssets(this@Home)
            }
        }

        val mDevice = deviceDatabaseHelper.getDeviceByModel("RMX3363")
        Log.e("TAG", "Device: " + mDevice?.branding + " " + mDevice?.name + " " + mDevice?.device + " " + mDevice?.model)

    }

    private fun setOnclickListeners() {

        binding.settingsImageView.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

    }


    private fun setupBottomNavigation() {

        binding.viewPagerBottomNav.adapter =
            BottomNavFragmentAdapter(supportFragmentManager, lifecycle)

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    binding.viewPagerBottomNav.setCurrentItem(0, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle(getString(R.string.app_name))
                }

                R.id.nav_device -> {
                    showHide(bottomNav = false, deviceTab = true, appsTab = false)
                    changeTitle("Details")
                }

                R.id.nav_test -> {
                    binding.viewPagerBottomNav.setCurrentItem(1, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle("Testing")

                }

                R.id.nav_apps -> {
                    showHide(bottomNav = false, deviceTab = false, appsTab = true)
                    changeTitle("Apps")

                }

                R.id.nav_report -> {
                    binding.viewPagerBottomNav.setCurrentItem(2, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle("Report")
                }
            }
            true
        }

        binding.bottomNavView.selectedItemId = R.id.nav_home
        binding.viewPagerBottomNav.currentItem = 0
        binding.viewPagerBottomNav.isUserInputEnabled = false


    }

    private fun changeTitle(title: String) {
        binding.title.text = title
    }

    private fun showHide(bottomNav: Boolean, deviceTab: Boolean, appsTab: Boolean) {

        binding.viewPagerBottomNav.visibility = if (bottomNav) View.VISIBLE else View.GONE
        binding.viewPagerDevice.visibility = if (deviceTab) View.VISIBLE else View.GONE
        binding.viewPagerApps.visibility = if (appsTab) View.VISIBLE else View.GONE

        binding.tabLayoutApps.visibility = if (appsTab) View.VISIBLE else View.GONE
        binding.tabLayoutDevice.visibility = if (deviceTab) View.VISIBLE else View.GONE

    }


    private fun setUpDeviceTabs() {

        binding.tabLayoutDevice.visibility = View.GONE

        val tabList = listOf(
            "CPU",
            "GPU",
            "OS",
            "System",
            "Display",
            "RAM",
            "Storage",
            "Battery",
            "Camera",
            "Connection",
            "Network",
            "GSM",
            "Sensor",
            "Thermal",
            "Codec",
            "Feature",
            "Misc"
        )

        binding.viewPagerDevice.adapter = DeviceFragmentAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayoutDevice, binding.viewPagerDevice) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }

    private fun setUpAppsTabs() {

        binding.tabLayoutApps.visibility = View.GONE

        val tabList = listOf("User", "System", "Permission", "Analytics")

        binding.viewPagerApps.adapter = AppsFragmentAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayoutApps, binding.viewPagerApps) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }


}