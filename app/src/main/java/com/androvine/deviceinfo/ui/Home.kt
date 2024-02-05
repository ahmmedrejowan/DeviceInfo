package com.androvine.deviceinfo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.androvine.deviceinfo.fragments.bottomNav.HomeFragment
import com.androvine.deviceinfo.fragments.bottomNav.ReportFragment
import com.androvine.deviceinfo.fragments.bottomNav.TestFragment

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

        val cpuTab = binding.tabLayoutDevice.newTab().setText("CPU")
        val gpuTab = binding.tabLayoutDevice.newTab().setText("GPU")
        val osTab = binding.tabLayoutDevice.newTab().setText("OS")
        val systemTab = binding.tabLayoutDevice.newTab().setText("System")
        val displayTab = binding.tabLayoutDevice.newTab().setText("Display")
        val ramTab = binding.tabLayoutDevice.newTab().setText("RAM")
        val storageTab = binding.tabLayoutDevice.newTab().setText("Storage")
        val batteryTab = binding.tabLayoutDevice.newTab().setText("Battery")
        val cameraTab = binding.tabLayoutDevice.newTab().setText("Camera")
        val connectionTab = binding.tabLayoutDevice.newTab().setText("Connection")
        val networkTab = binding.tabLayoutDevice.newTab().setText("Network")
        val gsmTab = binding.tabLayoutDevice.newTab().setText("GSM")
        val sensorTab = binding.tabLayoutDevice.newTab().setText("Sensor")
        val thermalTab = binding.tabLayoutDevice.newTab().setText("Thermal")
        val codecTab = binding.tabLayoutDevice.newTab().setText("Codec")
        val featureTab = binding.tabLayoutDevice.newTab().setText("Feature")
        val miscTab = binding.tabLayoutDevice.newTab().setText("Misc")

        binding.tabLayoutDevice.addTab(cpuTab)
        binding.tabLayoutDevice.addTab(gpuTab)
        binding.tabLayoutDevice.addTab(osTab)
        binding.tabLayoutDevice.addTab(systemTab)
        binding.tabLayoutDevice.addTab(displayTab)
        binding.tabLayoutDevice.addTab(ramTab)
        binding.tabLayoutDevice.addTab(storageTab)
        binding.tabLayoutDevice.addTab(batteryTab)
        binding.tabLayoutDevice.addTab(cameraTab)
        binding.tabLayoutDevice.addTab(connectionTab)
        binding.tabLayoutDevice.addTab(networkTab)
        binding.tabLayoutDevice.addTab(gsmTab)
        binding.tabLayoutDevice.addTab(sensorTab)
        binding.tabLayoutDevice.addTab(thermalTab)
        binding.tabLayoutDevice.addTab(codecTab)
        binding.tabLayoutDevice.addTab(featureTab)
        binding.tabLayoutDevice.addTab(miscTab)


    }

    private fun setUpAppsTabs() {

        binding.tabLayoutApps.visibility = View.GONE

        val userTab = binding.tabLayoutApps.newTab().setText("User")
        val systemTab = binding.tabLayoutApps.newTab().setText("System")
        val permissionTab = binding.tabLayoutApps.newTab().setText("Permission")
        val analyticsTab = binding.tabLayoutApps.newTab().setText("Analytics")

        binding.tabLayoutApps.addTab(userTab)
        binding.tabLayoutApps.addTab(systemTab)
        binding.tabLayoutApps.addTab(permissionTab)
        binding.tabLayoutApps.addTab(analyticsTab)

    }

    private fun setUpFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }


}