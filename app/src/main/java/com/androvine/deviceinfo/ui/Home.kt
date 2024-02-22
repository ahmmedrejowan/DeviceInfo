package com.androvine.deviceinfo.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsFragmentAdapter
import com.androvine.deviceinfo.adapter.BottomNavFragmentAdapter
import com.androvine.deviceinfo.adapter.DeviceFragmentAdapter
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.androvine.deviceinfo.utils.OpenGLInfoListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by viewModel()

    private var currentState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupBottomNavigation()

        setUpDeviceTabs()

        setUpAppsTabs()

        setOnclickListeners()


        val myGLSurfaceView = binding.glSurfaceView
        val openGLInfoListener = object : OpenGLInfoListener {
            override fun onOpenGLInfoReceived(
                vendor: String?,
                renderer: String?,
                version: String?,
                shaderVersion: String?,
                extensions: String?
            ) {
                deviceDetailsViewModel.getGpuData(vendor, renderer, version, shaderVersion, extensions)
            }
        }
        myGLSurfaceView.setOpenGLInfoListener(openGLInfoListener)


        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display
        } else {
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager?)!!.defaultDisplay
        }

        deviceDetailsViewModel.getDisplayData(display)




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
                    currentState = 0
                }

                R.id.nav_device -> {
                    showHide(bottomNav = false, deviceTab = true, appsTab = false)
                    changeTitle("Details")
                    currentState = 1
                }

                R.id.nav_test -> {
                    binding.viewPagerBottomNav.setCurrentItem(1, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle("Testing")
                    currentState = 2

                }

                R.id.nav_apps -> {
                    showHide(bottomNav = false, deviceTab = false, appsTab = true)
                    changeTitle("Apps")
                    currentState = 3

                }

                R.id.nav_monitor -> {
                    binding.viewPagerBottomNav.setCurrentItem(2, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle("Monitor")
                    currentState = 4
                }
            }
            true
        }

        binding.bottomNavView.selectedItemId = R.id.nav_home
        binding.viewPagerBottomNav.currentItem = 0
        binding.viewPagerBottomNav.isUserInputEnabled = false


    }

    override fun onResume() {
        super.onResume()

        Log.e("TAG", "onResume: currentState $currentState")
        switchToState(currentState)

    }

    private fun switchToState(cState: Int) {
        when (cState) {
            0 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_home
                binding.viewPagerBottomNav.setCurrentItem(0, false)
                showHide(bottomNav = true, deviceTab = false, appsTab = false)
                changeTitle(getString(R.string.app_name))
            }

            1 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_device
                showHide(bottomNav = false, deviceTab = true, appsTab = false)
                changeTitle("Details")
            }

            2 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_monitor
                binding.viewPagerBottomNav.setCurrentItem(2, false)
                showHide(bottomNav = true, deviceTab = false, appsTab = false)
                changeTitle("Monitor")
            }

            3 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_apps
                showHide(bottomNav = false, deviceTab = false, appsTab = true)
                changeTitle("Apps")
            }

            4 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_test
                binding.viewPagerBottomNav.setCurrentItem(1, false)
                showHide(bottomNav = true, deviceTab = false, appsTab = false)
                changeTitle("Testing")
            }
        }

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
        )

        for (i in tabList) {
            binding.tabLayoutDevice.addTab(binding.tabLayoutDevice.newTab().setText(i))
        }

        binding.viewPagerDevice.adapter = DeviceFragmentAdapter(supportFragmentManager, lifecycle)


        binding.tabLayoutDevice.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPagerDevice.setCurrentItem(tab!!.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPagerDevice.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayoutDevice.selectTab(binding.tabLayoutDevice.getTabAt(position))
            }
        })



    }

    private fun setUpAppsTabs() {

        binding.tabLayoutApps.visibility = View.GONE

        val tabList = listOf("User", "System", "Permission", "Analytics")

        binding.viewPagerApps.adapter = AppsFragmentAdapter(supportFragmentManager, lifecycle)


        for (i in tabList) {
            binding.tabLayoutApps.addTab(binding.tabLayoutApps.newTab().setText(i))
        }

        binding.tabLayoutApps.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPagerApps.setCurrentItem(tab!!.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPagerApps.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayoutApps.selectTab(binding.tabLayoutApps.getTabAt(position))
            }
        })

    }


}