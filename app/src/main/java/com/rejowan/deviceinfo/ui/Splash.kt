package com.rejowan.deviceinfo.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.rejowan.deviceinfo.utils.IntroRepository
import org.koin.androidx.viewmodel.ext.android.viewModel

class Splash : AppCompatActivity() {

    private val deviceDetailsViewModel: DeviceDetailsViewModel by viewModel()
    private lateinit var introUtils : IntroRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        introUtils = IntroRepository(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION") window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        deviceDetailsViewModel.copyDatabaseFromAssets()

        if (!introUtils.isFirstTimeLaunch()) {
            deviceDetailsViewModel.getSystemData()
            deviceDetailsViewModel.getOsData()
            deviceDetailsViewModel.getCpuData()
            deviceDetailsViewModel.getMemoryData()
            deviceDetailsViewModel.getStorageData()
            deviceDetailsViewModel.getCameraData()
            deviceDetailsViewModel.getAllApps()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (introUtils.isFirstTimeLaunch()) {
                startActivity(Intent(this@Splash, Intro::class.java))
                finish()
            } else {
                startActivity(Intent(this@Splash, Home::class.java))
                finish()
            }
        }, 2000)


    }
}