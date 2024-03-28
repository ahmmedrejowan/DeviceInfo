package com.rejowan.deviceinfo.fragments.device

import android.app.Dialog
import android.content.Context.SENSOR_SERVICE
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.adapter.SensorListAdapter
import com.rejowan.deviceinfo.databinding.DialogSensorAccelerometerBinding
import com.rejowan.deviceinfo.databinding.DialogSensorDefaultBinding
import com.rejowan.deviceinfo.databinding.FragmentSensorBinding
import com.rejowan.chart.components.XAxis
import com.rejowan.chart.data.Entry
import com.rejowan.chart.data.LineData
import com.rejowan.chart.data.LineDataSet
import com.rejowan.chart.interfaces.datasets.ILineDataSet


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
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        sensorDefaultBinding.title.text = sensor.name
        sensorDefaultBinding.type.text = sensor.stringType
        sensorDefaultBinding.manufacturer.text = sensor.vendor
        sensorDefaultBinding.power.text = sensor.power.toString()
        sensorDefaultBinding.maxRange.text = sensor.maximumRange.toString()
        sensorDefaultBinding.resolution.text = sensor.resolution.toString()
        sensorDefaultBinding.wakeUp.text = if (sensor.isWakeUpSensor) "Yes" else "No"

        sensorDefaultBinding.dismissButton.setOnClickListener {
            dialog.dismiss()
        }

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

        binding.accelerometerModel.text = sensor.vendor

        binding.accelerometerLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.accelerometerModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }


    }


    private fun setupMagneticSensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.magneticFieldModel.text = sensor.vendor

        binding.magneticFieldLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.magneticFieldModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }
    }

    private fun setupRotationVectorSensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.rotationVectorModel.text = sensor.vendor

        binding.rotationVectorLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.rotationVectorModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

    }

    private fun setupGyroscopeSensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.gyroscopeModel.text = sensor.vendor

        binding.gyroscopeLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.gyroscopeModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

    }

    private fun setupProximitySensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.proximityModel.text = sensor.vendor

        binding.proximityLayout.setOnClickListener {
            showDialog2(sensorManager, sensor)
        }

        binding.proximityModel.setOnClickListener {
            showDialog2(sensorManager, sensor)
        }

    }

    private fun setupLightSensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.lightModel.text = sensor.vendor

        binding.lightLayout.setOnClickListener {
            showDialog2(sensorManager, sensor)
        }

        binding.lightModel.setOnClickListener {
            showDialog2(sensorManager, sensor)
        }

    }

    private fun setupGravitySensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.gravityModel.text = sensor.vendor

        binding.gravityLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.gravityModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

    }

    private fun setupLinearAccelerationSensor(sensorManager: SensorManager, sensor: Sensor) {
        binding.linearAccelerationModel.text = sensor.vendor

        binding.linearAccelerationLayout.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

        binding.linearAccelerationModel.setOnClickListener {
            showDialog(sensorManager, sensor)
        }

    }

    private fun showDialog(sensorManager: SensorManager, sensor: Sensor) {

        val dialog = Dialog(requireContext())
        val sensorDefaultBinding = DialogSensorAccelerometerBinding.inflate(layoutInflater)
        dialog.setContentView(sensorDefaultBinding.root)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        sensorDefaultBinding.title.text = sensor.name
        sensorDefaultBinding.type.text = sensor.stringType
        sensorDefaultBinding.manufacturer.text = sensor.vendor
        sensorDefaultBinding.power.text = sensor.power.toString()
        sensorDefaultBinding.maxRange.text = sensor.maximumRange.toString()
        sensorDefaultBinding.resolution.text = sensor.resolution.toString()
        sensorDefaultBinding.wakeUp.text = if (sensor.isWakeUpSensor) "Yes" else "No"


        sensorDefaultBinding.lineChart.description.isEnabled = false
        sensorDefaultBinding.lineChart.setPinchZoom(true)
        sensorDefaultBinding.lineChart.setDrawGridBackground(false)
        sensorDefaultBinding.lineChart.isDragEnabled = true
        sensorDefaultBinding.lineChart.setScaleEnabled(true)
        sensorDefaultBinding.lineChart.setTouchEnabled(true)

        val xAxis: XAxis = sensorDefaultBinding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = requireActivity().getColor(R.color.textColor)
        xAxis.setDrawAxisLine(false)

        sensorDefaultBinding.lineChart.axisRight.isEnabled = false

        val yAxis = sensorDefaultBinding.lineChart.axisLeft
        yAxis.textColor = requireActivity().getColor(R.color.textColor)

        sensorDefaultBinding.lineChart.axisLeft.setDrawGridLines(false)

        sensorDefaultBinding.lineChart.animateXY(1500, 1500)


        val data = LineData()
        sensorDefaultBinding.lineChart.data = data


        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

                val x = event!!.values[0]
                val y = event.values[1]
                val z = event.values[2]

                sensorDefaultBinding.xAxis.text = "$x"
                sensorDefaultBinding.xAxis.setTextColor(requireActivity().getColor(R.color.colorPrimary))
                sensorDefaultBinding.yAxis.text = "$y"
                sensorDefaultBinding.yAxis.setTextColor(requireActivity().getColor(R.color.red))
                sensorDefaultBinding.zAxis.text = "$z"
                sensorDefaultBinding.zAxis.setTextColor(requireActivity().getColor(R.color.green))

                val lineData = sensorDefaultBinding.lineChart.data

                if (lineData != null) {
                    var set: ILineDataSet? = lineData.getDataSetByIndex(0)
                    var set2: ILineDataSet? = lineData.getDataSetByIndex(1)
                    var set3: ILineDataSet? = lineData.getDataSetByIndex(2)

                    if (set == null || set2 == null || set3 == null) {
                        set = createSet()
                        lineData.addDataSet(set)

                        set2 = createSet2()
                        lineData.addDataSet(set2)

                        set3 = createSet3()
                        lineData.addDataSet(set3)
                    }


                    lineData.addEntry(Entry(set.entryCount.toFloat(), x), 0)
                    lineData.addEntry(Entry(set2.entryCount.toFloat(), y), 1)
                    lineData.addEntry(Entry(set3.entryCount.toFloat(), z), 2)

                    if (set.entryCount > 25) {
                        set.removeFirst()
                        for (i in 0 until set.entryCount) {
                            val entry = set.getEntryForIndex(i)
                            entry.x = entry.x - 1
                        }
                    }

                    if (set2.entryCount > 25) {
                        set2.removeFirst()
                        for (i in 0 until set2.entryCount) {
                            val entry = set2.getEntryForIndex(i)
                            entry.x = entry.x - 1
                        }
                    }

                    if (set3.entryCount > 25) {
                        set3.removeFirst()
                        for (i in 0 until set3.entryCount) {
                            val entry = set3.getEntryForIndex(i)
                            entry.x = entry.x - 1
                        }
                    }

                    lineData.notifyDataChanged()
                    sensorDefaultBinding.lineChart.notifyDataSetChanged()
                    sensorDefaultBinding.lineChart.invalidate()
                }


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        sensorManager.registerListener(
            sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorDefaultBinding.dismissButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            sensorManager.unregisterListener(sensorEventListener)
        }

        dialog.show()
    }

    private fun showDialog2(sensorManager: SensorManager, sensor: Sensor) {

        val dialog = Dialog(requireContext())
        val sensorDefaultBinding = DialogSensorAccelerometerBinding.inflate(layoutInflater)
        dialog.setContentView(sensorDefaultBinding.root)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        sensorDefaultBinding.title.text = sensor.name
        sensorDefaultBinding.type.text = sensor.stringType
        sensorDefaultBinding.manufacturer.text = sensor.vendor
        sensorDefaultBinding.power.text = sensor.power.toString()
        sensorDefaultBinding.maxRange.text = sensor.maximumRange.toString()
        sensorDefaultBinding.resolution.text = sensor.resolution.toString()
        sensorDefaultBinding.wakeUp.text = if (sensor.isWakeUpSensor) "Yes" else "No"


        sensorDefaultBinding.lineChart.description.isEnabled = false
        sensorDefaultBinding.lineChart.setPinchZoom(true)
        sensorDefaultBinding.lineChart.setDrawGridBackground(false)
        sensorDefaultBinding.lineChart.isDragEnabled = true
        sensorDefaultBinding.lineChart.setScaleEnabled(true)
        sensorDefaultBinding.lineChart.setTouchEnabled(true)

        val xAxis: XAxis = sensorDefaultBinding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = requireActivity().getColor(R.color.textColor)
        xAxis.setDrawAxisLine(false)

        sensorDefaultBinding.lineChart.axisRight.isEnabled = false

        val yAxis = sensorDefaultBinding.lineChart.axisLeft
        yAxis.textColor = requireActivity().getColor(R.color.textColor)

        sensorDefaultBinding.lineChart.axisLeft.setDrawGridLines(false)

        sensorDefaultBinding.lineChart.animateXY(1500, 1500)


        val data = LineData()
        sensorDefaultBinding.lineChart.data = data


        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

                val x = event!!.values[0]

                sensorDefaultBinding.xAxis.text = "$x"
                sensorDefaultBinding.xAxis.setTextColor(requireActivity().getColor(R.color.colorPrimary))


                val lineData = sensorDefaultBinding.lineChart.data

                if (lineData != null) {
                    var set: ILineDataSet? = lineData.getDataSetByIndex(0)


                    if (set == null) {
                        set = createSet()
                        lineData.addDataSet(set)


                    }


                    lineData.addEntry(Entry(set.entryCount.toFloat(), x), 0)

                    if (set.entryCount > 25) {
                        set.removeFirst()
                        for (i in 0 until set.entryCount) {
                            val entry = set.getEntryForIndex(i)
                            entry.x = entry.x - 1
                        }
                    }


                    lineData.notifyDataChanged()
                    sensorDefaultBinding.lineChart.notifyDataSetChanged()
                    sensorDefaultBinding.lineChart.invalidate()
                }


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        sensorManager.registerListener(
            sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorDefaultBinding.dismissButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            sensorManager.unregisterListener(sensorEventListener)
        }

        dialog.show()
    }


    private fun createSet(): LineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
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

    private fun createSet2(): LineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.4f
        lineDataSet.setDrawFilled(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(requireActivity().getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = requireActivity().getColor(R.color.red)
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        return lineDataSet
    }

    private fun createSet3(): LineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.4f
        lineDataSet.setDrawFilled(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(requireActivity().getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = requireActivity().getColor(R.color.green)
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        return lineDataSet
    }


}