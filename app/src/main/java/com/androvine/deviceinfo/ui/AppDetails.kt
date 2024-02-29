package com.androvine.deviceinfo.ui

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.databinding.ActivityAppDetailsBinding
import java.io.File

class AppDetails : AppCompatActivity() {

    private val binding by lazy { ActivityAppDetailsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val packageName = intent.getStringExtra("packageName")
        val pm = packageManager
        val packageInfo = try {
            packageName?.let {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(it, PackageManager.PackageInfoFlags.of(0))
                } else {
                    pm.getPackageInfo(it, 0)
                }
            }

        } catch (e: PackageManager.NameNotFoundException) {
            null
        }

        if (packageInfo != null) {
            setupUI(packageInfo)
        }


    }

    private fun setupUI(packageInfo: PackageInfo) {

        val name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
        val packageName = packageInfo.packageName
        val version = packageInfo.versionName
        val targetApi = "API ${packageInfo.applicationInfo.targetSdkVersion}"
        val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
        val appSize = packageInfo.applicationInfo.sourceDir.let { File(it).length() / (1024f * 1024f) }

        binding.appName.text = name
        binding.appName2.text = name
        binding.appPackage.text = packageName
        binding.appVersionName.text = version
        binding.appTargetAPI.text = targetApi
        binding.appIcon.setImageDrawable(appIcon)
        binding.appSize.text = String.format("%.2f", appSize) + " MB"


    }


}

