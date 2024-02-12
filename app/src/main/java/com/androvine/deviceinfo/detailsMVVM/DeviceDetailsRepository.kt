package com.androvine.deviceinfo.detailsMVVM

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.androvine.deviceinfo.dataClasses.CpuDBModel
import com.androvine.deviceinfo.dataClasses.DeviceDBModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getBuildDateFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCPUGovernor
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCpuMaxFrequency
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getFormattedUptime
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getGooglePlayServicesVersion
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getOpenGLES
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getSecurityPatchFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isDeviceRooted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isSeamlessUpdateSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isTrebleSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.parseProcModels
import com.androvine.deviceinfo.detailsMVVM.dataClass.CpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.GpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.OsDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.SystemDataModel
import com.androvine.icons.AndroidVersionIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.FileReader
import java.util.TimeZone


class DeviceDetailsRepository(private val context: Context) {

    private val cpuDatabaseHelper = CpuDatabaseHelper(context)
    private val deviceDatabaseHelper = DeviceDatabaseHelper(context)

    private val _systemDataModel = MutableLiveData<SystemDataModel?>()
    val systemDataModel get() = _systemDataModel

    private val _osDataModel = MutableLiveData<OsDataModel?>()
    val osDataModel get() = _osDataModel

    private val _cpuDBModel = MutableLiveData<CpuDataModel?>()
    val cpuDataModel get() = _cpuDBModel

    private val _gpuDataModel = MutableLiveData<GpuDataModel?>()
    val gpuDataModel get() = _gpuDataModel

    suspend fun copyDatabaseFromAssets() {

        withContext(Dispatchers.IO) {
            cpuDatabaseHelper.copyDatabaseFromAssets(context)
            deviceDatabaseHelper.copyDatabaseFromAssets(context)
        }

    }

    suspend fun getCpuDataByModel(model: String): CpuDBModel? = withContext(Dispatchers.IO) {
        val cpuData = cpuDatabaseHelper.getCpuDataByModel(model)
        cpuData
    }

    suspend fun getDeviceDataByModel(model: String): DeviceDBModel? = withContext(Dispatchers.IO) {
        val deviceData = deviceDatabaseHelper.getDeviceByModel(model)
        deviceData
    }

    suspend fun getDeviceDataByDevice(device: String): DeviceDBModel? =
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByDevice(device)
            deviceData
        }


    suspend fun getSystemData() {

        withContext(Dispatchers.IO) {


            val deferredDeviceData = async { getDeviceDataByModel(Build.MODEL) }
            var name = deferredDeviceData.await()?.name
            if (name == null) {
                name = getDeviceDataByDevice(Build.DEVICE)?.name
            }
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

            val deferredVersionData =
                async { AndroidVersionIcon().getVersionByApiLevel(Build.VERSION.SDK_INT) }
            val versionData = deferredVersionData.await()

            val deferredIsRooted = async { isDeviceRooted() }
            val isRooted = deferredIsRooted.await()

            val deferredGlesVersion =
                async { withContext(Dispatchers.Main) { getOpenGLES(context) } }
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

    suspend fun getCpuData() {
        withContext(Dispatchers.IO) {

            val fileReader = FileReader("/proc/cpuinfo")
            val bufferedReader = fileReader.buffered()

            val deferredCpuProcInfo = async { bufferedReader.readText() }
            val cpuProcInfo = deferredCpuProcInfo.await()

            var processorModel = ""
            var processorModelAll = ""
            var processorModelAbove31 = ""

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                processorModelAbove31 = Build.SOC_MODEL

            }

            val hardwareLine = cpuProcInfo.lines().find { it.trim().startsWith("Hardware") }
            val hardwareText = hardwareLine?.substringAfter(":")?.trim()

            processorModelAll = if (hardwareText!!.contains("Qualcomm Technologies, Inc")) {
                val lastlineTemp = hardwareText.trim().removePrefix("Qualcomm Technologies, Inc")
                lastlineTemp.trim()
            } else {
                hardwareText.trim()
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {


                processorModel =
                    if (processorModelAll.trim().lowercase() == processorModelAbove31.trim()
                            .lowercase()
                    ) {
                        processorModelAll
                    } else {


                        if (processorModelAll.trim().lowercase()
                                .contains(processorModelAbove31.trim().lowercase())
                        ) {
                            processorModelAll
                        } else if (processorModelAbove31.trim().lowercase()
                                .contains(processorModelAll.trim().lowercase())
                        ) {
                            processorModelAbove31


                        } else {
                            processorModelAbove31
                        }
                    }
            } else {

                processorModel = processorModelAll
            }

            Log.e("TAG", "processorModel: $processorModel")


            var cpuDBModel: CpuDBModel? = null

            val deferredCpuData = async { getCpuDataByModel(processorModel) }
            cpuDBModel = deferredCpuData.await()

            if (cpuDBModel == null) {
                if (processorModel == processorModelAll) {
                    processorModel = processorModelAbove31
                    val deferredCpuDataAbove31 = async { getCpuDataByModel(processorModel) }
                    cpuDBModel = deferredCpuDataAbove31.await()
                }
            }

            if (cpuDBModel == null) {
                cpuDBModel = CpuDBModel(
                    model = processorModel, name = "", fab = "", gpu = "", core = "", vendor = ""
                )
            }


            val procList = parseProcModels(cpuProcInfo)
            bufferedReader.close()
            fileReader.close()


            val cpuData = CpuDataModel(
                model = processorModel,
                name = cpuDBModel.name,
                manufacturer = cpuDBModel.vendor,
                architecture = System.getProperty("os.arch"),
                fab = cpuDBModel.fab,
                coreCount = procList.size.toString(),
                coreDetail = cpuDBModel.core,
                frequency = getCpuMaxFrequency().toString() + " MHz",
                governor = getCPUGovernor(),
                cpuBit = if (Build.SUPPORTED_64_BIT_ABIS.isNotEmpty()) "64" else "32",
                cpuFeatures = procList[0].features,
                cpuImplementer = procList[0].implementer,
                cpuPart = procList[0].part,
                cpuRevision = procList[0].revision,
                cpuVariant = procList[0].variant,
                procInfo = procList
            )

            _cpuDBModel.postValue(cpuData)

        }
    }


    suspend fun getGpuData(
        vendor: String?,
        renderer: String?,
        version: String?,
        shaderVersion: String?,
        extensions: String?
    ) {
        withContext(Dispatchers.IO) {

            val deferredGlesVersion =
                async { withContext(Dispatchers.Main) { getOpenGLES(context) } }
            val openGlVersion = deferredGlesVersion.await()

            val gpuDataModel = GpuDataModel(
                name = getCpuDataByModel(cpuDataModel.value?.model!!)?.gpu ?: "N/A",
                vendor = vendor ?: "N/A",
                renderer = renderer ?: "N/A",
                version = version ?: "N/A",
                glEsVersion = openGlVersion,
                shaderVersion = shaderVersion ?: "N/A",
                extensions = extensions ?: "N/A"
            )
            _gpuDataModel.postValue(gpuDataModel)

        }

    }


}
