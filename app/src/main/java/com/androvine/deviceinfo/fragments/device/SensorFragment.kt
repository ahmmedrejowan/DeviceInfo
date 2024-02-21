package com.androvine.deviceinfo.fragments.device

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentSensorBinding


class SensorFragment : Fragment() {

    private val binding by lazy {
        FragmentSensorBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllSensorInfo()


    }

    private fun getAllSensorInfo() {

        val sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager

        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.top1.text = "${sensorList.size} sensors"

        for (sensor in sensorList) {
            Log.e("SensorFragment", "name: ${sensor.name} type: ${sensor.type} type name: ${sensor.stringType}")


        }




    }

}