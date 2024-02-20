package com.androvine.deviceinfo.fragments.device

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.adapter.SensorListAdapter
import com.androvine.deviceinfo.databinding.FragmentThermalBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader


class ThermalFragment : Fragment() {

    private val binding by lazy {
        FragmentThermalBinding.inflate(layoutInflater)
    }

    private val adapter: SensorListAdapter by lazy {
        SensorListAdapter(
            mutableMapOf()
        )
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getThermalInfo()

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            getThermalInfo()
            handler.postDelayed(runnable, 1000)
        }


    }

    private fun getThermalInfo() {

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val temperatureData = getTemperatureData()

                withContext(Dispatchers.Main) {

                    val sortedTemperatureData = temperatureData.toSortedMap()

                    binding.top1.text = sortedTemperatureData.size.toString() + " sensors"
                    adapter.updateList(sortedTemperatureData)
                }
            }
        }


    }

    private fun getTemperatureData(): Map<String, String> {

        val temperatureData = mutableMapOf<String, String>()

        for (i in 1..80) {
            val typeCmd = "cat /sys/devices/virtual/thermal/thermal_zone$i/type"
            val tempCmd = "cat /sys/devices/virtual/thermal/thermal_zone$i/temp"

            val typeProcess = Runtime.getRuntime().exec(typeCmd)
            val tempProcess = Runtime.getRuntime().exec(tempCmd)

            val typeReader = BufferedReader(InputStreamReader(typeProcess.inputStream))
            val tempReader = BufferedReader(InputStreamReader(tempProcess.inputStream))

            val type = typeReader.readLine()?.trim() ?: continue
            val temp = tempReader.readLine()?.trim()?.toFloatOrNull() ?: continue

            if (type == "soc") {
                temperatureData[type] = "$temp °C"
            } else {

                if (type.contains("pmi") || type.contains("ibat") || type.contains("vbat") || type.contains("bcl") || temp < 0) {
                    // do nothing
                } else {
                    temperatureData[type] = String.format("%.1f", temp / 1000) + " °C"
                }

            }


            typeReader.close()
            tempReader.close()
        }

        return temperatureData
    }

}