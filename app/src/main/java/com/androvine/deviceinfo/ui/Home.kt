package com.androvine.deviceinfo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsFragmentAdapter
import com.androvine.deviceinfo.adapter.BottomNavFragmentAdapter
import com.androvine.deviceinfo.adapter.DeviceFragmentAdapter
import com.androvine.deviceinfo.dataClasses.CpuDBModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databinding.ActivityHomeBinding
import com.androvine.icons.AndroidVersionIcon
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.log

class Home : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private var currentState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupBottomNavigation()

        setUpDeviceTabs()

        setUpAppsTabs()

        setOnclickListeners()



    }


//    private fun testMethod() {
//
//        val cpuDatabaseHelper = CpuDatabaseHelper(this)
//
//        val processorsMap = parseProcessors(this)
//
//        processorsMap.forEach { (modelName, processor) ->
//            println("Model: $modelName")
//            println("Vendor: ${processor.VENDOR}")
//            println("Name: ${processor.NAME}")
//            println("Fab: ${processor.FAB}")
//            println("CPU: ${processor.CPU}")
//            println("Memory: ${processor.MEMORY}")
//            println("Bandwidth: ${processor.BANDWIDTH}")
//            println("Channels: ${processor.CHANNELS}")
//            println()
//            val cpuDBModel = CpuDBModel(
//                model = modelName,
//                name = processor.NAME,
//                fab = processor.FAB,
//                gpu = "",
//                core = processor.CPU,
//                vendor = processor.VENDOR
//            )
//
//            cpuDatabaseHelper.addCpuData(cpuDBModel)
//        }
//
//    }

//    fun parseProcessors(context: Context): Map<String, Processor> {
//        val inputStream = context.resources.openRawResource(R.raw.aa)
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val gson = Gson()
//        val processors: Map<String, Processor> = gson.fromJson(reader, object : com.google.gson.reflect.TypeToken<Map<String, Processor>>() {}.type)
//        inputStream.close()
//        return processors
//    }

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

                R.id.nav_report -> {
                    binding.viewPagerBottomNav.setCurrentItem(2, false)
                    showHide(bottomNav = true, deviceTab = false, appsTab = false)
                    changeTitle("Report")
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
                binding.bottomNavView.selectedItemId = R.id.nav_test
                binding.viewPagerBottomNav.setCurrentItem(1, false)
                showHide(bottomNav = true, deviceTab = false, appsTab = false)
                changeTitle("Testing")

            }

            3 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_apps
                showHide(bottomNav = false, deviceTab = false, appsTab = true)
                changeTitle("Apps")

            }

            4 -> {
                binding.bottomNavView.selectedItemId = R.id.nav_report
                binding.viewPagerBottomNav.setCurrentItem(2, false)
                showHide(bottomNav = true, deviceTab = false, appsTab = false)
                changeTitle("Report")
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