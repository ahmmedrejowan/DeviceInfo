package com.rejowan.deviceinfo.ui.test

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.databinding.ActivityAccelerometerBinding
import com.rejowan.deviceinfo.utils.TestingPrefs
import com.rejowan.chart.components.XAxis
import com.rejowan.chart.data.Entry
import com.rejowan.chart.data.LineData
import com.rejowan.chart.data.LineDataSet
import com.rejowan.chart.interfaces.datasets.ILineDataSet

class Accelerometer : AppCompatActivity() {

    private val binding by lazy { ActivityAccelerometerBinding.inflate(layoutInflater) }
    private val testingPrefs by lazy { TestingPrefs(this) }

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.yesButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.ACCELEROMETER.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.ACCELEROMETER.key, false)
            finish()
        }

        binding.lineChart.description.isEnabled = false
        binding.lineChart.setPinchZoom(true)
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(true)
        binding.lineChart.setTouchEnabled(true)

        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = getColor(R.color.textColor)
        xAxis.setDrawAxisLine(false)

        binding.lineChart.axisRight.isEnabled = false

        val yAxis = binding.lineChart.axisLeft
        yAxis.textColor = getColor(R.color.textColor)

        binding.lineChart.axisLeft.setDrawGridLines(false)

        binding.lineChart.animateXY(1500, 1500)


        val data = LineData()
        binding.lineChart.data = data


        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

                val x = event!!.values[0]
                val y = event.values[1]
                val z = event.values[2]

                binding.xAxis.text = "$x"
                binding.xAxis.setTextColor(getColor(R.color.colorPrimary))
                binding.yAxis.text = "$y"
                binding.yAxis.setTextColor(getColor(R.color.red))
                binding.zAxis.text = "$z"
                binding.zAxis.setTextColor(getColor(R.color.green))

                val lineData = binding.lineChart.data

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
                    binding.lineChart.notifyDataSetChanged()
                    binding.lineChart.invalidate()
                }


            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        sensorManager.registerListener(
            sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )

    }


    private fun createSet(): LineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.4f
        lineDataSet.setDrawFilled(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = getColor(R.color.colorPrimary)
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
        lineDataSet.setCircleColor(getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = getColor(R.color.red)
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
        lineDataSet.setCircleColor(getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = getColor(R.color.green)
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        return lineDataSet
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener)
    }

}