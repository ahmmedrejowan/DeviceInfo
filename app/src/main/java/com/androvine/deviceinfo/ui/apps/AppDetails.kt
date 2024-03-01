package com.androvine.deviceinfo.ui.apps

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.adapter.AppDetailsAdapter
import com.androvine.deviceinfo.databinding.ActivityAppDetailsBinding
import com.androvine.deviceinfo.databinding.BottomSheetAppDetailDefaultBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

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
        val versionName = packageInfo.versionName
        val versionCode = if (SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            packageInfo.versionCode.toString()
        }
        val targetApi = "API ${packageInfo.applicationInfo.targetSdkVersion}"
        val minApi = "API ${packageInfo.applicationInfo.minSdkVersion}"
        val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
        val appSize =
            packageInfo.applicationInfo.sourceDir.let { File(it).length() / (1024f * 1024f) }
        val installedDate = packageInfo.firstInstallTime
        val updatedDate = packageInfo.lastUpdateTime
        val installer = packageManager.getInstallerPackageName(packageName)
        val installerName = installer?.let {
            try {
                packageManager.getApplicationLabel(packageManager.getApplicationInfo(it, 0))
                    .toString()
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }

        val permission = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions

        var listOfPermissions: List<String> = emptyList()
        if (permission != null) {
            listOfPermissions = permission.toList()
        }

        val activities = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES
        ).activities

        val listOfActivities: List<ActivityInfo>
        var listOfActivityNames: List<String> = emptyList()

        activities?.let {
            listOfActivities = activities.toList()
            listOfActivityNames = listOfActivities.map { it.name }
        }

        val services = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SERVICES
        ).services

        var listOfServices: List<ServiceInfo>
        var listOfServiceNames: List<String> = emptyList()

        services?.let {
            listOfServices = services.toList()
            listOfServiceNames = listOfServices.map { it.name }
        }


        val receivers = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_RECEIVERS
        ).receivers

        val listOfReceivers: List<ActivityInfo>
        var listOfReceiverNames: List<String> = emptyList()

        receivers?.let {
            listOfReceivers = receivers.toList()
            listOfReceiverNames = listOfReceivers.map { it.name }
        }


        val providers = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PROVIDERS
        ).providers

        val listOfProviders: List<ProviderInfo>
        var listOfProviderNames: List<String> = emptyList()

        providers?.let {
            listOfProviders = providers.toList()
            listOfProviderNames = listOfProviders.map { it.name }
        }


        val listOfNativeLibraries: Array<String> =
            packageInfo.applicationInfo.nativeLibraryDir?.let { File(it).list() } ?: emptyArray()



        binding.appName.text = name
        binding.appName2.text = name
        binding.appPackage.text = packageName
        binding.versionCode.text = versionCode
        binding.versionName.text = versionName
        binding.targetApi.text = targetApi
        binding.minApi.text = minApi
        binding.appIcon.setImageDrawable(appIcon)
        binding.appSize.text = String.format("%.2f", appSize) + " MB"
        binding.installedAt.text = formatTime(installedDate)
        binding.updatedAt.text = formatTime(updatedDate)
        binding.installer.text = installerName ?: "Unknown"

        binding.permissionChip.text = "Permissions (${listOfPermissions.size})"
        binding.activityChip.text = "Activities (${listOfActivityNames.size})"
        binding.serviceChip.text = "Services (${listOfServiceNames.size})"
        binding.receiverChip.text = "Receivers (${listOfReceiverNames.size})"
        binding.providerChip.text = "Providers (${listOfProviderNames.size})"
        binding.nativeChip.text = "Native (${listOfNativeLibraries.size})"



        binding.permissionChip.setOnClickListener {
            if (listOfPermissions.isNotEmpty()) {
                showBottomDialog(listOfPermissions)
            } else {
                Toast.makeText(this, "No permissions found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.activityChip.setOnClickListener {
            if (listOfActivityNames.isNotEmpty()) {
                showBottomDialog(listOfActivityNames)
            } else {
                Toast.makeText(this, "No activities found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.serviceChip.setOnClickListener {
            if (listOfServiceNames.isNotEmpty()) {
                showBottomDialog(listOfServiceNames)
            } else {
                Toast.makeText(this, "No services found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.receiverChip.setOnClickListener {
            if (listOfReceiverNames.isNotEmpty()) {
                showBottomDialog(listOfReceiverNames)
            } else {
                Toast.makeText(this, "No receivers found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.providerChip.setOnClickListener {
            if (listOfProviderNames.isNotEmpty()) {
                showBottomDialog(listOfProviderNames)
            } else {
                Toast.makeText(this, "No providers found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.nativeChip.setOnClickListener {
            if (listOfNativeLibraries.isNotEmpty()) {
                showBottomDialog(listOfNativeLibraries.toList())
            } else {
                Toast.makeText(this, "No native libraries found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.openApp.setOnClickListener {
            try {
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open app", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.appInfo.setOnClickListener {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        binding.playStore.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$packageName")
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                startActivity(intent)
            }
        }



    }

    private fun showBottomDialog(listOfPermissions: List<String>) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetBinding: BottomSheetAppDetailDefaultBinding =
            BottomSheetAppDetailDefaultBinding.inflate(
                layoutInflater
            )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        bottomSheetDialog.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )

        val adapter = AppDetailsAdapter(listOfPermissions.toMutableList())
        bottomSheetBinding.recyclerView.adapter = adapter
        bottomSheetBinding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        bottomSheetDialog.show()


    }


    private fun formatTime(installedDate: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a, dd MMM yyy", Locale.US)
        return dateFormat.format(installedDate)
    }


}

