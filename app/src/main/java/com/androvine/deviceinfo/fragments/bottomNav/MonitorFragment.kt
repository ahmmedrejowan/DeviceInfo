package com.androvine.deviceinfo.fragments.bottomNav

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.CpuCoreUsageListAdapter
import com.androvine.deviceinfo.databinding.FragmentMonitorBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.androvine.deviceinfo.fragments.device.BatteryFragment
import com.rejowan.chart.components.XAxis
import com.rejowan.chart.data.Entry
import com.rejowan.chart.data.LineData
import com.rejowan.chart.data.LineDataSet
import com.rejowan.chart.interfaces.datasets.ILineDataSet
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.BufferedReader
import java.io.FileReader


class MonitorFragment : Fragment() {

    private val binding by lazy { FragmentMonitorBinding.inflate(layoutInflater) }

    private lateinit var handler: Handler
    private lateinit var uptimeRunnable: Runnable
    private val adapter by lazy { CpuCoreUsageListAdapter(mutableMapOf()) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    private val batteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent == null) {
                return
            }

            binding.abv.attachBatteryIntent(intent)


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
            handler.postDelayed(uptimeRunnable, 1500)
        }


        binding.cpuFreqRecyclerView.adapter = adapter
        binding.cpuFreqRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)

        setupCPULoad()

        setupRamUsage()

        setupBattery()


        deviceDetailsViewModel.memoryDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.arcProgressRam.progress = it.usagePercent.toFloat()
                binding.ramUsage.text = "${it.usedMemory.replace("GB","").trim()} / ${it.totalMemory}"


                val lineData = binding.ramChart.data
                if (lineData != null) {
                    var set: ILineDataSet? = lineData.getDataSetByIndex(0)


                    if (set == null) {
                        set = createSet()
                        lineData.addDataSet(set)


                    }
                    // used memory 2.29 GB
                    val usedMemory = it.usedMemory.replace("GB", "").trim().toFloat()

                    lineData.addEntry(Entry(set.entryCount.toFloat(), usedMemory), 0)

                    if (set.entryCount > 25) {
                        set.removeFirst()
                        for (i in 0 until set.entryCount) {
                            val entry = set.getEntryForIndex(i)
                            entry.x = entry.x - 1
                        }
                    }


                    lineData.notifyDataChanged()
                    binding.ramChart.notifyDataSetChanged()
                    binding.ramChart.invalidate()
                }


            }

        }


    }

    private fun setupBattery() {
        binding.batteryChart.description.isEnabled = false
        binding.batteryChart.setPinchZoom(true)
        binding.batteryChart.setDrawGridBackground(false)
        binding.batteryChart.isDragEnabled = true
        binding.batteryChart.setScaleEnabled(true)
        binding.batteryChart.setTouchEnabled(true)

        val xAxis: XAxis = binding.batteryChart.xAxis
        xAxis.isEnabled = false

        binding.batteryChart.axisLeft.isEnabled = false
        binding.batteryChart.axisRight.isEnabled = true

        val yAxis = binding.batteryChart.axisRight
        yAxis.textColor = requireActivity().getColor(R.color.textColor)
        binding.batteryChart.axisLeft.setDrawGridLines(false)
        binding.batteryChart.animateXY(1500, 1500)

        binding.batteryChart.legend.isEnabled = false

        val data = LineData()
        binding.batteryChart.data = data

    }

    private fun setupRamUsage() {
        binding.ramChart.description.isEnabled = false
        binding.ramChart.setPinchZoom(true)
        binding.ramChart.setDrawGridBackground(false)
        binding.ramChart.isDragEnabled = true
        binding.ramChart.setScaleEnabled(true)
        binding.ramChart.setTouchEnabled(true)

        val xAxis: XAxis = binding.ramChart.xAxis
        xAxis.isEnabled = false

        binding.ramChart.axisLeft.isEnabled = false
        binding.ramChart.axisRight.isEnabled = true

        val yAxis = binding.ramChart.axisRight
        yAxis.textColor = requireActivity().getColor(R.color.textColor)
        binding.ramChart.axisLeft.setDrawGridLines(false)
        binding.ramChart.animateXY(1500, 1500)

        binding.ramChart.legend.isEnabled = false

        val data = LineData()
        binding.ramChart.data = data

    }

    private fun setupCPULoad() {

        binding.cpuLoadChart.description.isEnabled = false
        binding.cpuLoadChart.setPinchZoom(true)
        binding.cpuLoadChart.setDrawGridBackground(false)
        binding.cpuLoadChart.isDragEnabled = true
        binding.cpuLoadChart.setScaleEnabled(true)
        binding.cpuLoadChart.setTouchEnabled(true)

        val xAxis: XAxis = binding.cpuLoadChart.xAxis
        xAxis.isEnabled = false

        binding.cpuLoadChart.axisLeft.isEnabled = false
        binding.cpuLoadChart.axisRight.isEnabled = true

        val yAxis = binding.cpuLoadChart.axisRight
        yAxis.textColor = requireActivity().getColor(R.color.textColor)
        binding.cpuLoadChart.axisLeft.setDrawGridLines(false)
        binding.cpuLoadChart.animateXY(1500, 1500)

        binding.cpuLoadChart.legend.isEnabled = false

        val data = LineData()
        binding.cpuLoadChart.data = data


    }

    private fun updateUsage() {

        updateRam()

        updateCPU()

        updateBattery()

    }

    private fun updateBattery() {

        val lineData = binding.batteryChart.data
        if (lineData != null) {
            var set: ILineDataSet? = lineData.getDataSetByIndex(0)


            if (set == null) {
                set = createSet()
                lineData.addDataSet(set)


            }

            lineData.addEntry(
                Entry(
                    set.entryCount.toFloat(),
                    BatteryFragment.getAmperage(requireContext()).toFloat()
                ), 0
            )

            if (set.entryCount > 25) {
                set.removeFirst()
                for (i in 0 until set.entryCount) {
                    val entry = set.getEntryForIndex(i)
                    entry.x = entry.x - 1
                }
            }


            lineData.notifyDataChanged()
            binding.batteryChart.notifyDataSetChanged()
            binding.batteryChart.invalidate()


            if (BatteryFragment.getAmperage(requireContext()).contains("-")) {
                binding.batteryUsageText.text = "Discharging (mAh)"
                binding.batteryUsage.text = BatteryFragment.getAmperage(requireContext()) + " mAh"
            } else {
                binding.batteryUsageText.text = "Charging (mAh)"
                binding.batteryUsage.text = BatteryFragment.getAmperage(requireContext()) + " mAh"
            }
        }


    }

    private fun updateCPU() {

        val cpuCurrentFrequency = getCurrentFrequencies()
        val cpuMaxFrequencies = getMaxCpuFrequencies()
        val numCores = Runtime.getRuntime().availableProcessors()

        val totalCpuCurrentFrequency = cpuCurrentFrequency.values.sum()
        val totalCpuMaxFrequency = cpuMaxFrequencies.values.sum()

        val cpuUsage = (totalCpuCurrentFrequency.toFloat() / totalCpuMaxFrequency.toFloat()) * 100


        if (binding.arcProgressCpu.progress.toInt() != cpuUsage.toInt()) {
            binding.arcProgressCpu.progress = cpuUsage
        }


        val currentFreqInGHz = totalCpuCurrentFrequency / numCores / 1000000f
        val maxFreqInGHz = totalCpuMaxFrequency / numCores / 1000000f

        binding.cpuUsage.text = String.format("%.2f / %.2f GHz", currentFreqInGHz, maxFreqInGHz)

        adapter.updateList(cpuCurrentFrequency)


        val lineData = binding.cpuLoadChart.data
        if (lineData != null) {
            var set: ILineDataSet? = lineData.getDataSetByIndex(0)


            if (set == null) {
                set = createSet()
                lineData.addDataSet(set)


            }

            lineData.addEntry(
                Entry(
                    set.entryCount.toFloat(), (totalCpuCurrentFrequency / 8 / 1000).toFloat()
                ), 0
            )

            if (set.entryCount > 25) {
                set.removeFirst()
                for (i in 0 until set.entryCount) {
                    val entry = set.getEntryForIndex(i)
                    entry.x = entry.x - 1
                }
            }


            lineData.notifyDataChanged()
            binding.cpuLoadChart.notifyDataSetChanged()
            binding.cpuLoadChart.invalidate()
        }

    }

    private fun updateRam() {

        deviceDetailsViewModel.getMemoryData()
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

    private fun createSet(): LineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.cubicIntensity = 0.4f
        lineDataSet.setDrawFilled(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(requireActivity().getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = requireActivity().getColor(R.color.colorPrimary)
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawValues(false)
        return lineDataSet
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
}