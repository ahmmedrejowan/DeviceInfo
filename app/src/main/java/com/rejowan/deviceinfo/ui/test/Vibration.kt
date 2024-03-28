package com.rejowan.deviceinfo.ui.test

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.rejowan.deviceinfo.databinding.ActivityVibrationBinding
import com.rejowan.deviceinfo.utils.TestingPrefs

class Vibration : AppCompatActivity() {

    private val binding by lazy { ActivityVibrationBinding.inflate(layoutInflater) }

    private val testingPrefs by lazy { TestingPrefs(this) }

    private lateinit var vibrator: Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(0, 1000, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 0)
            }
        } else {
            Log.e("Vibration", "Device does not have vibration")
        }

        binding.yesButton.setOnClickListener {
            vibrator.cancel()
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.VIBRATION.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            vibrator.cancel()
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.VIBRATION.key, false)
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
    }


}