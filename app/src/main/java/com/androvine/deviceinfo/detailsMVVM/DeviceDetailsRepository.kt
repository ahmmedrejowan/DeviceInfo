package com.androvine.deviceinfo.detailsMVVM

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.androvine.deviceinfo.dataClasses.CpuDataModel
import com.androvine.deviceinfo.dataClasses.DeviceDataModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getBuildDateFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getFormattedUptime
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getGooglePlayServicesVersion
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getOpenGLES
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getSecurityPatchFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isDeviceRooted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isSeamlessUpdateSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isTrebleSupported
import com.androvine.deviceinfo.detailsMVVM.dataClass.OsDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.SystemDataModel
import com.androvine.icons.AndroidVersionIcon
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DeviceDetailsRepository(private val context: Context) {

    private val cpuDatabaseHelper = CpuDatabaseHelper(context)
    private val deviceDatabaseHelper = DeviceDatabaseHelper(context)

    private val _systemDataModel = MutableLiveData<SystemDataModel?>()
    val systemDataModel get() = _systemDataModel

    private val _osDataModel = MutableLiveData<OsDataModel?>()
    val osDataModel get() = _osDataModel

    suspend fun copyDatabaseFromAssets() {

        withContext(Dispatchers.IO) {
            cpuDatabaseHelper.copyDatabaseFromAssets(context)
            deviceDatabaseHelper.copyDatabaseFromAssets(context)
        }

    }

    suspend fun getCpuDataByModel(model: String): CpuDataModel? = withContext(Dispatchers.IO) {
        val cpuData = cpuDatabaseHelper.getCpuDataByModel(model)
        cpuData
    }

    suspend fun getDeviceDataByModel(model: String): DeviceDataModel? =
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByModel(model)
            deviceData
        }

    suspend fun getDeviceDataByDevice(device: String): DeviceDataModel? =
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByDevice(device)
            deviceData
        }


    suspend fun getSystemData() {

        withContext(Dispatchers.IO) {


            val deferredDeviceData = async { getDeviceDataByModel(Build.MODEL) }
            var name = deferredDeviceData.await()?.name
            if (name == null) {
                name = Build.MODEL
            }

            val systemData = SystemDataModel(
                brand = Build.BRAND,
                name = name,
                model = Build.MODEL,
                device = Build.DEVICE,
                product = Build.PRODUCT,
                manufacturer = Build.MANUFACTURER,
                hardware = Build.HARDWARE,
                board = Build.BOARD,
                radio = Build.getRadioVersion()
            )
            _systemDataModel.postValue(systemData)
        }

    }

    suspend fun getOsData() {
        withContext(Dispatchers.IO) {

            val deferredVersionData = async { AndroidVersionIcon().getVersionByApiLevel(Build.VERSION.SDK_INT) }
            val versionData = deferredVersionData.await()

            val deferredIsRooted = async { isDeviceRooted() }
            val isRooted = deferredIsRooted.await()

            val deferredGlesVersion = async { withContext(Dispatchers.Main) { getOpenGLES(context) } }
            val openGlVersion = deferredGlesVersion.await()



            val osData = OsDataModel(
                apiLevel = Build.VERSION.SDK_INT,
                version = versionData?.version,
                codeName = versionData?.codeName,
                versionName = versionData?.name,
                bootloader = Build.BOOTLOADER,
                securityPatch = getSecurityPatchFormatted(Build.VERSION.SECURITY_PATCH),
                build = Build.DISPLAY,
                buildDate = getBuildDateFormatted(Build.TIME),
                fingerprint = Build.FINGERPRINT,
                architecture = System.getProperty("os.arch"),
                abis = Build.SUPPORTED_ABIS.toList(),
                abis32 = Build.SUPPORTED_32_BIT_ABIS.toList(),
                abis64 = Build.SUPPORTED_64_BIT_ABIS.toList(),
                kernelVersion = System.getProperty("os.version"),
                isRooted = isRooted,
                javaVm = System.getProperty("java.vm.name"),
                javaVmVersion = System.getProperty("java.vm.version"),
                incremental = Build.VERSION.INCREMENTAL,
                googlePlayServicesVersion = getGooglePlayServicesVersion(context),
                uptime = getFormattedUptime(android.os.SystemClock.elapsedRealtime()),
                timeZone = TimeZone.getDefault().id + " - " + TimeZone.getDefault().displayName,
                openGlVersion = openGlVersion,
                trebleSupported = isTrebleSupported(),
                seamlessSupported = isSeamlessUpdateSupported(context)
                )
            _osDataModel.postValue(osData)
        }
    }



}
