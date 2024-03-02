package com.androvine.deviceinfo.fragments.bottomNav

import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentTestBinding
import com.androvine.deviceinfo.ui.test.Accelerometer
import com.androvine.deviceinfo.ui.test.Flashlight
import com.androvine.deviceinfo.ui.test.LightSensor
import com.androvine.deviceinfo.ui.test.Microphone
import com.androvine.deviceinfo.ui.test.MultiTouch
import com.androvine.deviceinfo.ui.test.Proximity
import com.androvine.deviceinfo.ui.test.Speaker
import com.androvine.deviceinfo.ui.test.Vibration
import com.androvine.deviceinfo.ui.test.VolumeButtons
import com.androvine.deviceinfo.utils.TestingPrefs


class TestFragment : Fragment() {

    private val binding by lazy { FragmentTestBinding.inflate(layoutInflater) }
    private val testingPrefs by lazy { TestingPrefs(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.multiTouchTextView.setOnClickListener {
            startActivity(Intent(requireContext(), MultiTouch::class.java))
        }

        binding.flaslightTextView.setOnClickListener {
           startActivity(Intent(requireContext(), Flashlight::class.java))
        }

        binding.speakerTextView.setOnClickListener {
            startActivity(Intent(requireContext(), Speaker::class.java))
        }

        binding.microphoneTextView.setOnClickListener {
            startActivity(Intent(requireContext(), Microphone::class.java))
        }

        binding.proximitySensorTextView.setOnClickListener {
            startActivity(Intent(requireContext(), Proximity::class.java))
        }

        binding.accelerometerTextView.setOnClickListener {
            startActivity(Intent(requireContext(), Accelerometer::class.java))
        }

        binding.lightSensorTextView.setOnClickListener {
            startActivity(Intent(requireContext(), LightSensor::class.java))
        }

        binding.vibrationTextView.setOnClickListener {
            startActivity(Intent(requireContext(), Vibration::class.java))
        }

        binding.volumeButtonsTextView.setOnClickListener {
            startActivity(Intent(requireContext(), VolumeButtons::class.java))
        }

        val sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as android.hardware.SensorManager

        val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximitySensor == null) {
            binding.proximitySensorLinearLayout.visibility = View.GONE
        }

        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor == null) {
            binding.lightSensorLinearLayout.visibility = View.GONE
        }

        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor == null) {
            binding.accelerometerLinearLayout.visibility = View.GONE
        }


    }


    override fun onResume() {
        super.onResume()
        getAllTestingPrefs()
    }

    private fun getAllTestingPrefs() {

        val allTestingPrefs = testingPrefs.getAllTestingPrefs()
        val multiTouch = allTestingPrefs[TestingPrefs.TestingPrefsKey.MULTI_TOUCH.key]
        val flash = allTestingPrefs[TestingPrefs.TestingPrefsKey.FLASH.key]
        val speaker = allTestingPrefs[TestingPrefs.TestingPrefsKey.SPEAKER.key]
        val microphone = allTestingPrefs[TestingPrefs.TestingPrefsKey.MICROPHONE.key]
        val proximity = allTestingPrefs[TestingPrefs.TestingPrefsKey.PROXIMITY.key]
        val accelerometer = allTestingPrefs[TestingPrefs.TestingPrefsKey.ACCELEROMETER.key]
        val lightSensor = allTestingPrefs[TestingPrefs.TestingPrefsKey.LIGHT_SENSOR.key]
        val vibration = allTestingPrefs[TestingPrefs.TestingPrefsKey.VIBRATION.key]
        val volume = allTestingPrefs[TestingPrefs.TestingPrefsKey.VOLUME.key]

        binding.multiTouchImageView.setImageResource(getImage(multiTouch))
        binding.flashlightImageView.setImageResource(getImage(flash))
        binding.speakerImageView.setImageResource(getImage(speaker))
        binding.microphoneImageView.setImageResource(getImage(microphone))
        binding.proximitySensorImageView.setImageResource(getImage(proximity))
        binding.accelerometerImageView.setImageResource(getImage(accelerometer))
        binding.lightSensorImageView.setImageResource(getImage(lightSensor))
        binding.vibrationImageView.setImageResource(getImage(vibration))
        binding.volumeButtonsImageView.setImageResource(getImage(volume))


    }

    private fun getImage(b: Boolean?): Int {
        return when (b) {
            true -> {
                R.drawable.ic_ok
            }

            false -> {
                R.drawable.ic_error
            }

            else -> {
                R.drawable.ic_warning
            }
        }
    }

}