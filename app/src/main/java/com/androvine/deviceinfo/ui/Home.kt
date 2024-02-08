package com.androvine.deviceinfo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsFragmentAdapter
import com.androvine.deviceinfo.adapter.BottomNavFragmentAdapter
import com.androvine.deviceinfo.adapter.DeviceFragmentAdapter
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.androvine.deviceinfo.dbMVVM.DatabaseViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val databaseViewModel: DatabaseViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setupBottomNavigation()

        setUpDeviceTabs()

        setUpAppsTabs()

        setOnclickListeners()



        databaseViewModel.getCpuDataByModel("SM7325")
        databaseViewModel.cpuDataModel.observe(this) {
            val cpu = it
            Log.e("TAG", "CPU: " + cpu?.model + " " + cpu?.name + " " + cpu?.fab + " " + cpu?.gpu)
        }


        databaseViewModel.getDeviceDataByModel("RMX3363")
        databaseViewModel.deviceDataModel.observe(this) {
            val device = it
            Log.e("TAG", "Device: " + device?.branding + " " + device?.name + " " + device?.device + " " + device?.model)
        }


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