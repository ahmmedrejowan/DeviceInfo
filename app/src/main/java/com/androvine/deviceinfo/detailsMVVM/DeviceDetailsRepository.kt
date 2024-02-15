package com.androvine.deviceinfo.detailsMVVM

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.Settings
import android.util.Log
import android.view.Display
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.androvine.deviceinfo.dataClasses.CpuDBModel
import com.androvine.deviceinfo.dataClasses.DeviceDBModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.bytesToHuman
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.calculateAspectRatio
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.calculateScreenSizeInches
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getBuildDateFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCPUGovernor
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCpuMaxFrequency
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getFormattedUptime
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getGooglePlayServicesVersion
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getHDRCapabilities
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getOpenGLES
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getSecurityPatchFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isDeviceRooted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isSeamlessUpdateSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isTrebleSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.parseProcModels
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.roundUpMemorySize
import com.androvine.deviceinfo.detailsMVVM.dataClass.CpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.DisplayDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.GpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.MemoryDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.OsDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.SystemDataModel
import com.androvine.icons.AndroidVersionIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.util.Locale
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

    private val _displayDataModel = MutableLiveData<DisplayDataModel?>()
    val displayDataModel get() = _displayDataModel

    private val _memoryDataModel = MutableLiveData<MemoryDataModel?>()
    val memoryDataModel get() = _memoryDataModel

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

    suspend fun getDisplayData(display: Display?) {
        withContext(Dispatchers.IO) {

            val mode = display!!.mode
            val refreshRate = mode.refreshRate.toInt()
            val width = mode.physicalWidth
            val height = mode.physicalHeight
            val widthPx: Int
            val heightPx: Int

            context.resources.displayMetrics.run {
                widthPx = widthPixels
                heightPx = heightPixels
            }

            val xdpi = context.resources.displayMetrics.xdpi
            val ydpi = context.resources.displayMetrics.ydpi
            val density = context.resources.displayMetrics.density.toString()
            val densityDpi = context.resources.displayMetrics.densityDpi.toString()
            val aspectRatio = calculateAspectRatio(width, height)
            val supportedRefreshRates = display.supportedModes.map { it.refreshRate.toInt() }
            val refreshRatesString =
                supportedRefreshRates.joinToString(separator = " Hz, ") { it.toString() } + " Hz"

            val hdrCaps = getHDRCapabilities(display)
            val isHdrSupported = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                display.isHdr
            } else {
                false
            }

            val colorGamut = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                display.isWideColorGamut
            } else {
                false
            }

            val brightnessMode = Settings.System.getInt(
                context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE
            )

            val brightnessValue =
                Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)

            val orientation = when (context.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
                Configuration.ORIENTATION_PORTRAIT -> "Portrait"
                else -> "Unknown"
            }

            val displayData = DisplayDataModel(
                resolution = "$width x $height",
                screenDensity = xdpi.toInt().toString(),
                density = density,
                densityDpi = densityDpi,
                size = calculateScreenSizeInches(width, height, xdpi.toInt()) + " inches",
                aspectRatio = aspectRatio,
                refreshRate = "$refreshRate Hz",
                supportedRefreshRates = refreshRatesString,
                hdr = if (isHdrSupported) "Supported" else "Not Supported",
                hdrCaps = hdrCaps,
                colorGamut = if (colorGamut) "Supported" else "Not Supported",
                absoluteResolution = "$widthPx x $heightPx",
                brightness = brightnessValue.toString(),
                brightnessMode = if (brightnessMode == 0) "Manual" else "Automatic",
                orientation = orientation
            )

            _displayDataModel.postValue(displayData)

        }
    }

    suspend fun getMemoryData() {
        withContext(Dispatchers.IO) {

            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)
            val totalMemory = memInfo.totalMem
            val availMemory = memInfo.availMem
            val threshold = memInfo.threshold
            val lowMemory = memInfo.lowMemory

            Log.e("TAG", "totalMemory: $totalMemory")
            Log.e("TAG", "availMemory: $availMemory")


            val usedMemory = totalMemory - availMemory
            val usedMemoryPercentage =
                ((usedMemory.toDouble() / totalMemory.toDouble()) * 100).toInt()

            val totalMemoryString = bytesToHuman(totalMemory)
            val availMemoryString = bytesToHuman(availMemory)
            val usedMemoryString = bytesToHuman(usedMemory)

            val memoryData = MemoryDataModel(
                advertisedMemory = bytesToHuman(roundUpMemorySize(totalMemory)),
                totalMemory = totalMemoryString,
                availableMemory = availMemoryString,
                usedMemory = usedMemoryString,
                usagePercent = usedMemoryPercentage,
                threshold = bytesToHuman(threshold),
                lowMemory = if (lowMemory) "Yes" else "No"
            )

            _memoryDataModel.postValue(memoryData)

        }
    }

    suspend fun getStorageData() {
        withContext(Dispatchers.IO) {

//            val fileReader = FileReader("/proc/self/mountinfo")
//            val bufferedReader = fileReader.buffered()
//
//            val deferredStorageInfo = async { bufferedReader.readText() }
//            val storageInfo = deferredStorageInfo.await()
//
//            Log.e("TAG", "getStorageData1: $storageInfo")

//            val fileReader2 = FileReader("/proc/mounts")
//            val bufferedReader2 = fileReader2.buffered()
//
//            val deferredStorageInfo2 = async { bufferedReader2.readText() }
//            val storageInfo2 = deferredStorageInfo2.await()
//
//            Log.e("TAG", "getStorageData2: $storageInfo2")


            val internalStoragePath = Environment.getDataDirectory().absolutePath
            val rootStoragePath = Environment.getRootDirectory().absolutePath
            val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath

            val system = "/"

            val systemStatFs = StatFs(system)
            val blockSizeSystem = systemStatFs.blockSizeLong
            val totalSizeSystem = systemStatFs.blockCountLong * blockSizeSystem
            val freeSizeSystem = systemStatFs.availableBlocksLong * blockSizeSystem
            val usedSizeSystem = totalSizeSystem - freeSizeSystem


            val internalStatFs = StatFs(internalStoragePath)
            val externalStatFs = StatFs(externalStoragePath)
            val rootStatFs = StatFs(rootStoragePath)

            val blockSize = internalStatFs.blockSizeLong
            val totalSize = internalStatFs.blockCountLong * blockSize
            val freeSize = internalStatFs.availableBlocksLong * blockSize
            val usedSize = totalSize - freeSize

            val externalBlockSize = externalStatFs.blockSizeLong
            val externalTotalSize = externalStatFs.blockCountLong * externalBlockSize
            val externalFreeSize = externalStatFs.availableBlocksLong * externalBlockSize
            val externalUsedSize = externalTotalSize - externalFreeSize

            val rootblockSize = rootStatFs.blockSizeLong
            val rootTotalSize = rootStatFs.blockCountLong * rootblockSize
            val rootFreeSize = rootStatFs.availableBlocksLong * rootblockSize
            val rootUsedSize = rootTotalSize - rootFreeSize

            Log.e("TAG", "totalSize: ${bytesToHuman(totalSize)}")
            Log.e("TAG", "freeSize: ${bytesToHuman(freeSize)}")
            Log.e("TAG", "usedSize: ${bytesToHuman(usedSize)}")

            Log.e("TAG", "externalTotalSize: ${bytesToHuman(externalTotalSize)}")
            Log.e("TAG", "externalFreeSize: ${bytesToHuman(externalFreeSize)}")
            Log.e("TAG", "externalUsedSize: ${bytesToHuman(externalUsedSize)}")

            Log.e("TAG", "rootTotalSize: ${bytesToHuman(rootTotalSize)}")
            Log.e("TAG", "rootFreeSize: ${bytesToHuman(rootFreeSize)}")
            Log.e("TAG", "rootUsedSize: ${bytesToHuman(rootUsedSize)}")

            Log.e("TAG", "systemTotalSize: ${bytesToHuman(totalSizeSystem)}")
            Log.e("TAG", "systemFreeSize: ${bytesToHuman(freeSizeSystem)}")
            Log.e("TAG", "systemUsedSize: ${bytesToHuman(usedSizeSystem)}")


            val storageManager =
                context.getSystemService(Context.STORAGE_SERVICE) as android.os.storage.StorageManager
            val storageVolumes = storageManager.storageVolumes
            for (storage in storageVolumes) {

                Log.e("TAG", "getStorageData: $storage")

                val isPrimary = storage.isPrimary
                val isEmulated = storage.isEmulated
                val isRemovable = storage.isRemovable
                val directory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    storage.directory!!.absolutePath
                } else {
                    "N/A"
                }
                val fsType = runCatching { File(directory).canonicalFile }.getOrNull()
                    ?.toString() // Get the canonical path of the filesystem
                val separatorIndex = fsType?.indexOf(" ") ?: -1
                val fsTypeFormatted =
                    if (separatorIndex != -1) fsType?.substring(0, separatorIndex) else fsType
                Log.e("TAG", "fsTypeFormatted: $fsTypeFormatted")


                // mount options


                val mediaStoreVolumeName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    storage.mediaStoreVolumeName
                } else {
                    "N/A"
                }

                val owner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    storage.owner
                } else {
                    "N/A"
                }

                val state = storage.state

                val description = storage.getDescription(context)


                val file = File(directory)
                val totalSpace = file.totalSpace
                val freeSpace = file.freeSpace
                val usedSpace = totalSpace - freeSpace
                Log.e("TAG", "totalSpace: ${bytesToHuman(totalSpace)}")
                Log.e("TAG", "freeSpace: ${bytesToHuman(freeSpace)}")
                Log.e("TAG", "usedSpace: ${bytesToHuman(usedSpace)}")



                Log.e("TAG", "isPrimary: $isPrimary")
                Log.e("TAG", "isEmulated: $isEmulated")
                Log.e("TAG", "isRemovable: $isRemovable")
                Log.e("TAG", "directory: $directory")
                Log.e("TAG", "mediaStoreVolumeName: $mediaStoreVolumeName")
                Log.e("TAG", "owner: $owner")
                Log.e("TAG", "state: $state")
                Log.e("TAG", "description: $description")


            }


            getVolumeStorageStats(context)


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val storageStatsManager =
//                    context.getSystemService(Context.STORAGE_STATS_SERVICE) as android.app.usage.StorageStatsManager
//                total
//
//
//            }


        }

    }

    private fun getVolumeStorageStats(context: Context) {
        val externalDirs = context.getExternalFilesDirs(null)
        val storageManager =
            context.getSystemService(AppCompatActivity.STORAGE_SERVICE) as StorageManager

        externalDirs.forEach { file ->
            val volumeName: String
            val totalStorageSpace: Long
            val freeStorageSpace: Long
            val isRemovable: Boolean
            val storageVolume = storageManager.getStorageVolume(file) ?: return
            val path : String
            if (storageVolume.isPrimary) {
                // internal storage
                volumeName = "Internal Storage"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val storageStatsManager =
                        context.getSystemService(AppCompatActivity.STORAGE_STATS_SERVICE) as StorageStatsManager
                    val uuid = StorageManager.UUID_DEFAULT
                    totalStorageSpace = storageStatsManager.getTotalBytes(uuid)
                    freeStorageSpace = storageStatsManager.getFreeBytes(uuid)
                    path = "N/A"


                } else {
                    totalStorageSpace = file.totalSpace
                    freeStorageSpace = file.freeSpace
                    path = file.absolutePath

                }
            } else {
                volumeName = storageVolume.uuid.toString()
                totalStorageSpace = file.totalSpace
                freeStorageSpace = file.freeSpace
                path = file.absolutePath


            }

            Log.e("TAG", "getVolumeStorageStats name: $volumeName")
            Log.e("TAG", "getVolumeStorageStats total: ${bytesToHuman(totalStorageSpace)}")
            Log.e("TAG", "getVolumeStorageStats free: ${bytesToHuman(freeStorageSpace)}")
            Log.e("TAG", "getVolumeStorageStats path: $path")


        }
    }


}
