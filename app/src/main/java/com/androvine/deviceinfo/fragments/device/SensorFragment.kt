package com.androvine.deviceinfo.fragments.device

import android.app.Dialog
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.adapter.SensorListAdapter
import com.androvine.deviceinfo.databinding.DialogSensorDefaultBinding
import com.androvine.deviceinfo.databinding.FragmentSensorBinding


class SensorFragment : Fragment() {

    private val binding by lazy {
        FragmentSensorBinding.inflate(layoutInflater)
    }


    val listOfSensors = mutableListOf<Sensor>()
    lateinit var adapter: SensorListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter =
            SensorListAdapter(mutableListOf(), object : SensorListAdapter.OnSensorItemClicked {
                override fun onItemClick(position: Int) {
                    showSensorDialog(listOfSensors[position])
                }
            })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        getAllSensorInfo()


    }

    private fun showSensorDialog(sensor: Sensor) {
        val dialog = Dialog(requireContext())
        val sensorDefaultBinding = DialogSensorDefaultBinding.inflate(layoutInflater)
        dialog.setContentView(sensorDefaultBinding.root)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        sensorDefaultBinding.title.text = sensor.name
        sensorDefaultBinding.type.text = sensor.stringType
        sensorDefaultBinding.manufacturer.text = sensor.vendor
        sensorDefaultBinding.power.text = sensor.power.toString()
        sensorDefaultBinding.maxRange.text = sensor.maximumRange.toString()
        sensorDefaultBinding.resolution.text = sensor.resolution.toString()
        sensorDefaultBinding.wakeUp.text = if (sensor.isWakeUpSensor) "Yes" else "No"


        dialog.show()

    }

    private fun getAllSensorInfo() {

        val sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager

        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.top1.text = "${sensorList.size} sensors"


        // get Accelerometer sensor
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor != null) {
            setupAccelerometerSensor(sensorManager, accelerometerSensor)
        } else {
            binding.accelerometerLayout.visibility = View.GONE
        }

        // get magnetic field sensor
        val magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magneticFieldSensor != null) {
            setupMagneticSensor(sensorManager, magneticFieldSensor)
        } else {
            binding.magneticFieldLayout.visibility = View.GONE
        }

        // get rotation vector sensor
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (rotationVectorSensor != null) {
            setupRotationVectorSensor(sensorManager, rotationVectorSensor)
        } else {
            binding.rotationVectorLayout.visibility = View.GONE
        }

        // get gyroscope sensor
        val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyroscopeSensor != null) {
            setupGyroscopeSensor(sensorManager, gyroscopeSensor)
        } else {
            binding.gyroscopeLayout.visibility = View.GONE
        }

        // get proximity sensor
        val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximitySensor != null) {
            setupProximitySensor(sensorManager, proximitySensor)
        } else {
            binding.proximityLayout.visibility = View.GONE
        }

        // get light sensor
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            setupLightSensor(sensorManager, lightSensor)
        } else {
            binding.lightLayout.visibility = View.GONE
        }

        // get gravity sensor
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (gravitySensor != null) {
            setupGravitySensor(sensorManager, gravitySensor)
        } else {
            binding.gravityLayout.visibility = View.GONE
        }

        // get linear acceleration sensor
        val linearAccelerationSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (linearAccelerationSensor != null) {
            setupLinearAccelerationSensor(sensorManager, linearAccelerationSensor)
        } else {
            binding.linearAccelerationLayout.visibility = View.GONE
        }

        // get all other sensors
        for (sensor in sensorList) {
            if (sensor.type != Sensor.TYPE_ACCELEROMETER && sensor.type != Sensor.TYPE_MAGNETIC_FIELD && sensor.type != Sensor.TYPE_ROTATION_VECTOR && sensor.type != Sensor.TYPE_GYROSCOPE && sensor.type != Sensor.TYPE_PROXIMITY && sensor.type != Sensor.TYPE_LIGHT && sensor.type != Sensor.TYPE_GRAVITY && sensor.type != Sensor.TYPE_LINEAR_ACCELERATION) {
                listOfSensors.add(sensor)
            }
        }
        setupOtherSensors(listOfSensors)

    }

    private fun setupOtherSensors(listOfSensors: MutableList<Sensor>) {
        adapter.updateList(listOfSensors)

    }

    private fun setupAccelerometerSensor(
        sensorManager: SensorManager, sensor: Sensor
    ) {


    }

    private fun setupMagneticSensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupRotationVectorSensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupGyroscopeSensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupProximitySensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupLightSensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupGravitySensor(sensorManager: SensorManager, sensor: Sensor) {

    }

    private fun setupLinearAccelerationSensor(sensorManager: SensorManager, sensor: Sensor) {

    }

}