package com.androvine.deviceinfo.ui.test

import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.databinding.ActivityFlashlightBinding
import com.androvine.deviceinfo.utils.TestingPrefs

class Flashlight : AppCompatActivity() {

    private val binding by lazy { ActivityFlashlightBinding.inflate(layoutInflater) }

    private val testingPrefs by lazy { TestingPrefs(this) }

    lateinit var cameraManager: CameraManager
    lateinit var cameraId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: Exception) {
            Log.e("Flashlight", "Error: $e")
        }

        binding.yesButton.setOnClickListener {
            cameraManager.setTorchMode(cameraId, false)
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.FLASH.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            cameraManager.setTorchMode(cameraId, false)
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.FLASH.key, false)
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.setTorchMode(cameraId, false)
    }


}