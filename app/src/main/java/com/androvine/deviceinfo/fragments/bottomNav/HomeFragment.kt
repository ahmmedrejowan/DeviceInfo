package com.androvine.deviceinfo.fragments.bottomNav

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentHomeBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.androvine.deviceinfo.fragments.device.BatteryFragment.Companion.getAmperage
import com.androvine.deviceinfo.utils.JavaUtils
import com.androvine.icons.AndroidVersionIcon
import com.androvine.icons.BrandIcons
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.BufferedReader
import java.io.FileReader
import java.text.DecimalFormat


class HomeFragment : Fragment() {


    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    private lateinit var handler: Handler
    private lateinit var uptimeRunnable: Runnable

    private val batteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent == null) {
                return
            }

            binding.abv.attachBatteryIntent(intent)

            val batteryTotalMah: Int = JavaUtils.getBatteryCapacity(context).toInt()

            var mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1).toDouble()
            java.lang.Double.isNaN(mVoltage)
            if (mVoltage > 12) {
                mVoltage /= 1000.0
            }
            val voltage = mVoltage.toFloat().toDouble()
            val voltageFormatted = String.format("%.2f", voltage)

            binding.capacityVoltage.text = "Volt: $voltageFormatted V  Cap: $batteryTotalMah mAh"

            val deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging =
                deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING || deviceStatus == BatteryManager.BATTERY_STATUS_FULL

            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat()
            val df = DecimalFormat("#.##")

            if (isCharging) {
                binding.batteryProgress.isIndeterminate = true
                binding.usage.visibility = View.GONE
            } else {
                binding.batteryProgress.isIndeterminate = false
                binding.batteryProgress.progress = (batteryPct * 100).toInt()
                binding.usage.visibility = View.VISIBLE
                binding.usage.text = df.format(batteryPct * 100) + "%"
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        uptimeRunnable = Runnable {
            updateUsage()
            handler.postDelayed(uptimeRunnable, 2500)
        }

        deviceDetailsViewModel.systemDataModel.observe(viewLifecycleOwner) {
            if (it != null) {
                val brandImage = BrandIcons().getBrandIconByName(it.brand!!)
                if (brandImage != null) {
                    binding.brandImage.setImageResource(brandImage)
                } else {
                    binding.brandImage.setImageResource(R.drawable.ic_android_partner)
                }
                binding.brandName.text = it.brand
                binding.deviceName.text = it.name
            }
        }

        deviceDetailsViewModel.osDataModel.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.androidVersion.text = it.versionName
                binding.androidIcon.setImageResource(AndroidVersionIcon().getVersionByApiLevel(it.apiLevel)!!.image)
            }
        }


        deviceDetailsViewModel.memoryDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.arcProgressRam.progress = it.usagePercent.toFloat()
                binding.ramUsage.text = "${it.usedMemory} / ${it.totalMemory}"


            }

        }

        deviceDetailsViewModel.storageDataModel.observe(viewLifecycleOwner) {
            if (it != null) {


                val usedPercentage = ((it.usedSize.toFloat() / it.totalSize.toFloat()) * 100)
                binding.storageProgress.progress = DecimalFormat("#.##").format(usedPercentage).toFloat()

                val sizeHtmlString =
                    "Total: ${DeviceDetailsUtils.bytesToHuman(it.totalSize)}  Used: ${
                        DeviceDetailsUtils.bytesToHuman(it.usedSize)
                    } <br/>Free: ${DeviceDetailsUtils.bytesToHuman(it.freeSize)}"

                binding.storageUsage.text =
                    Html.fromHtml(sizeHtmlString, Html.FROM_HTML_MODE_COMPACT)

            }

        }

        deviceDetailsViewModel.displayDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.displayResolution.text = it.resolution + " px"

            }

        }

        getAllApps()


        requireActivity().registerReceiver(
            batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )


    }

    private fun getAllApps() {

        val pm = requireActivity().packageManager
        val listOfApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            pm.getInstalledPackages(0)
        }

        binding.appCount.text = listOfApps.size.toString() + " Total"
    }


    private fun updateUsage() {

        updateRam()

        updateCPU()

        updateBattery()

    }

    private fun updateBattery() {
        if (getAmperage(requireContext()).contains("-")) {
            binding.chargingStatus.text = getAmperage(requireContext()) + " mAh (Discharging)"
            binding.chargingStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        } else {
            binding.chargingStatus.text = getAmperage(requireContext()) + " mAh (Charging)"
            binding.chargingStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

    }

    private fun updateCPU() {

        val cpuCurrentFrequency = getCurrentFrequencies()
        val cpuMaxFrequencies = getMaxCpuFrequencies()
        val numCores = Runtime.getRuntime().availableProcessors()

        val totalCpuCurrentFrequency = cpuCurrentFrequency.values.sum()
        val totalCpuMaxFrequency = cpuMaxFrequencies.values.sum()

        val cpuUsage = (totalCpuCurrentFrequency.toFloat() / totalCpuMaxFrequency.toFloat()) * 100


        if (binding.arcProgressCpu.progress.toInt() != cpuUsage.toInt()){
            binding.arcProgressCpu.progress = cpuUsage
        }

        val currentFreqInGHz = totalCpuCurrentFrequency / numCores / 1000000f
        val maxFreqInGHz = totalCpuMaxFrequency / numCores / 1000000f

        binding.cpuUsage.text = String.format("%.2f GHz / %.2f GHz", currentFreqInGHz, maxFreqInGHz)

    }


    private fun updateRam() {

        deviceDetailsViewModel.getMemoryData()
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(uptimeRunnable)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        handler.post(uptimeRunnable)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(batteryReceiver)
        handler.removeCallbacks(uptimeRunnable)
    }

    private fun getCurrentFrequencies(): Map<Int, Long> {
        val cpuFrequencies = mutableMapOf<Int, Long>()
        try {
            val numCores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until numCores) {
                val cpuFrequency = getSingleCurrentFrequency(i)
                cpuFrequencies[i] = cpuFrequency
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cpuFrequencies
    }

    private fun getSingleCurrentFrequency(coreNumber: Int): Long {
        var frequency: Long = 0
        try {
            val path = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_cur_freq"
            val reader = BufferedReader(FileReader(path))
            val line = reader.readLine()
            frequency = line.toLong() // Frequency is in kHz
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return frequency
    }

    private fun getMaxCpuFrequencies(): Map<Int, Long> {
        val maxCpuFrequencies = mutableMapOf<Int, Long>()
        try {
            val numCores = Runtime.getRuntime().availableProcessors()
            for (i in 0 until numCores) {
                val maxCpuFrequency = getMaxCpuFrequency(i)
                maxCpuFrequencies[i] = maxCpuFrequency
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return maxCpuFrequencies
    }

    private fun getMaxCpuFrequency(coreNumber: Int): Long {
        var maxFrequency: Long = 0
        try {
            val path = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/cpuinfo_max_freq"
            val reader = BufferedReader(FileReader(path))
            val line = reader.readLine()
            maxFrequency = line.toLong() // Frequency is in kHz
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return maxFrequency
    }

}