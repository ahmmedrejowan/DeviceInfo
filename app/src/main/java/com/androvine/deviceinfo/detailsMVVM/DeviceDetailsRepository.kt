package com.androvine.deviceinfo.detailsMVVM

import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
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
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getAllAntiBandingModes
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getAllExposerModes
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getAllFocusModes
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getAllSceneModes
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getAllWhiteBalanceModes
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getBuildDateFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCPUGovernor
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getCpuMaxFrequency
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getFormattedUptime
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getGooglePlayServicesVersion
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getHDRCapabilities
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getOpenGLES
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getSecurityPatchFormatted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getShutterSpeedRange
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isDeviceRooted
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isSeamlessUpdateSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.isTrebleSupported
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.parseProcModels
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.roundUpMemorySize
import com.androvine.deviceinfo.detailsMVVM.dataClass.CameraDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.CpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.DisplayDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.GpuDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.MemoryDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.MiniCameraModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.MiniStorageModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.OsDataModel
import com.androvine.deviceinfo.detailsMVVM.dataClass.StorageDataModel
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

    private val _displayDataModel = MutableLiveData<DisplayDataModel?>()
    val displayDataModel get() = _displayDataModel

    private val _memoryDataModel = MutableLiveData<MemoryDataModel?>()
    val memoryDataModel get() = _memoryDataModel

    private val _storageDataModel = MutableLiveData<StorageDataModel?>()
    val storageDataModel get() = _storageDataModel

    private val _cameraDataModel = MutableLiveData<CameraDataModel?>()
    val cameraDataModel get() = _cameraDataModel

    private val _allApps = MutableLiveData<MutableList<PackageInfo>>()
    val allApps get() = _allApps

    private val _userApps = MutableLiveData<MutableList<PackageInfo>>()
    val userApps get() = _userApps

    private val _systemApps = MutableLiveData<MutableList<PackageInfo>>()
    val systemApps get() = _systemApps

    suspend fun copyDatabaseFromAssets() {

        withContext(Dispatchers.IO) {
            cpuDatabaseHelper.copyDatabaseFromAssets(context)
            deviceDatabaseHelper.copyDatabaseFromAssets(context)
        }

    }

    private suspend fun getCpuDataByModel(model: String): CpuDBModel? =
        withContext(Dispatchers.IO) {
            val cpuData = cpuDatabaseHelper.getCpuDataByModel(model)
            cpuData
        }

    private suspend fun getDeviceDataByModel(model: String): DeviceDBModel? =
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByModel(model)
            deviceData
        }

    private suspend fun getDeviceDataByDevice(device: String): DeviceDBModel? =
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

            processorModelAll = if (hardwareText?.contains("Qualcomm Technologies, Inc") == true) {
                val lastlineTemp = hardwareText.trim().removePrefix("Qualcomm Technologies, Inc")
                lastlineTemp.trim()
            } else ({
                hardwareText?.trim()
            }).toString()


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
                    model = processorModel,
                    name = processorModel,
                    fab = "N/A",
                    gpu = "N/A",
                    core = "N/A",
                    vendor = "N/A"
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


            val listOfStorage = mutableListOf<MiniStorageModel>()

            var romSize: Long = 0
            var romFreeSize: Long = 0
            var romUsedSize: Long = 0

            val externalDirs = context.getExternalFilesDirs(null)
            val storageManager =
                context.getSystemService(AppCompatActivity.STORAGE_SERVICE) as StorageManager

            externalDirs.forEach { file ->
                //   val volumeName: String
                val totalStorageSpace: Long
                val freeStorageSpace: Long
                val storageVolume = storageManager.getStorageVolume(file) ?: return@forEach
                //   val path : String
                if (storageVolume.isPrimary) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val storageStatsManager =
                            context.getSystemService(AppCompatActivity.STORAGE_STATS_SERVICE) as StorageStatsManager
                        val uuid = StorageManager.UUID_DEFAULT
                        romSize = storageStatsManager.getTotalBytes(uuid)
                        romFreeSize = storageStatsManager.getFreeBytes(uuid)
                        romUsedSize = romSize - romFreeSize

                        val name = "Internal Storage"
                        val storagePath = "/storage/emulated/0"

                        val totalSpace = file.totalSpace
                        val freeSpace = file.freeSpace
                        val usedSpace = totalSpace - freeSpace

                        val miniStorageModel = MiniStorageModel(
                            totalSize = totalSpace,
                            usedSize = usedSpace,
                            freeSize = freeSpace,
                            storageName = name,
                            storagePath = storagePath
                        )
                        listOfStorage.add(miniStorageModel)


                    } else {
                        romSize = file.totalSpace
                        romFreeSize = file.freeSpace
                        romUsedSize = romSize - romFreeSize

                        val miniStorageModel = MiniStorageModel(
                            totalSize = file.totalSpace,
                            usedSize = file.totalSpace - file.freeSpace,
                            freeSize = file.freeSpace,
                            storageName = "Internal Storage",
                            storagePath = file.path
                        )
                        listOfStorage.add(miniStorageModel)


                    }
                } else {

                    val name = storageVolume.uuid.toString()
                    totalStorageSpace = file.totalSpace
                    freeStorageSpace = file.freeSpace
                    val path = "/storage/$name"

                    val miniStorageModel = MiniStorageModel(
                        totalSize = totalStorageSpace,
                        usedSize = totalStorageSpace - freeStorageSpace,
                        freeSize = freeStorageSpace,
                        storageName = "Removable Storage",
                        storagePath = path
                    )
                    listOfStorage.add(miniStorageModel)

                }


            }

            val storageData = StorageDataModel(
                totalSize = romSize,
                usedSize = romUsedSize,
                freeSize = romFreeSize,
                list = listOfStorage
            )

            _storageDataModel.postValue(storageData)

        }

    }

    suspend fun getCameraData() {
        withContext(Dispatchers.IO) {

            val miniCameraModelList = mutableListOf<MiniCameraModel>()

            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIds = cameraManager.cameraIdList

            for (i in cameraIds) {
                Log.e("TAG", "cameraId: $i")
            }

            val backCamera = cameraIds.find {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                facing == CameraCharacteristics.LENS_FACING_BACK
            }

            val frontCamera = cameraIds.find {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                facing == CameraCharacteristics.LENS_FACING_FRONT
            }

            if (!backCamera.isNullOrEmpty()) {
                miniCameraModelList.add(0, getCameraDetails(backCamera, cameraManager))
            }

            if (!frontCamera.isNullOrEmpty()) {
                miniCameraModelList.add(1, getCameraDetails(frontCamera, cameraManager))
            }


            val cameraData = CameraDataModel(
                cameraList = miniCameraModelList
            )

            _cameraDataModel.postValue(cameraData)


        }
    }

    private fun getCameraDetails(
        cameraID: String, cameraManager: CameraManager
    ): MiniCameraModel {

        val characteristics = cameraManager.getCameraCharacteristics(cameraID)
        val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
        val sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
        val aperture = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)
        val focalLength =
            characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
        val lensOpticalStabilization =
            characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION)
        val videoStabilization =
            characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES)
        val digitalZoom =
            characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)
        val shutterSpeed =
            characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE)
        val iso = characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE)
        val highestResolution =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val antiBanding =
            characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES)
        val exposerModes = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES)
        val focusModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)
        val whiteBalanceModes =
            characteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES)
        val sceneModes = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES)
        val flashSupport = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
        val orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        val cropFactor = characteristics.get(CameraCharacteristics.SCALER_CROPPING_TYPE)


        val name =
            if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) "Back Camera" else "Front Camera"
        val highestResolutionSize = highestResolution?.getOutputSizes(ImageFormat.JPEG)
            ?.maxByOrNull { it.height * it.width }
        val highestResolutionSizeFormatted =
            highestResolutionSize?.width.toString() + " x " + highestResolutionSize?.height.toString()
        val megaPixels =
            highestResolutionSize?.width?.times(highestResolutionSize.height)?.div(1000000f)
        val megaPixelsFormatted = String.format("%.2f", megaPixels)
        val apertureFormatted = aperture?.max()?.toString()
        val focalLengthFormatted = focalLength?.max()?.toString()
        val sensorSizeFormatted =
            sensorSize?.width.toString() + " x " + sensorSize?.height.toString() + " mm"
        val shutterSpeedRangeFormatted = getShutterSpeedRange(shutterSpeed)
        val isoRangeFormatted = iso?.lower.toString() + " - " + iso?.upper.toString()
        val antiBandingFormatted = getAllAntiBandingModes(antiBanding)
        val exposerModesFormatted = getAllExposerModes(exposerModes)
        val focusModesFormatted = getAllFocusModes(focusModes)
        val whiteBalanceFormatted = getAllWhiteBalanceModes(whiteBalanceModes)
        val sceneModeFormatted = getAllSceneModes(sceneModes)
        val flashFormatted = if (flashSupport == true) "Supported" else "Not Supported"
        val orientationFormatted = orientation.toString() + "°"
        val cropFactorFormatted =
            if (cropFactor == CameraCharacteristics.SCALER_CROPPING_TYPE_CENTER_ONLY) "Center Only" else "Freeform"
        val stabilizationFormatted =
            if (lensOpticalStabilization?.contains(CameraCharacteristics.LENS_OPTICAL_STABILIZATION_MODE_ON) == true) "Supported" else "Not Supported"
        val videoStabilizationFormatted =
            if (videoStabilization?.contains(CameraCharacteristics.CONTROL_VIDEO_STABILIZATION_MODE_ON) == true) "Supported" else "Not Supported"
        val digitalZoomFormatted = digitalZoom.toString() + "x"

        return MiniCameraModel(
            cameraName = name,
            megapixels = "$megaPixelsFormatted MP",
            aperture = "f $apertureFormatted",
            focalLength = "$focalLengthFormatted mm",
            sensorSize = sensorSizeFormatted,
            shutterSpeed = shutterSpeedRangeFormatted,
            iso = isoRangeFormatted,
            highestResolution = highestResolutionSizeFormatted,
            antiBanding = antiBandingFormatted,
            autoExposer = exposerModesFormatted,
            autoFocus = focusModesFormatted,
            whiteBalance = whiteBalanceFormatted,
            sceneMode = sceneModeFormatted,
            flash = flashFormatted,
            orientation = orientationFormatted,
            opticalStabilization = stabilizationFormatted,
            videoStabilization = videoStabilizationFormatted,
            digitalZoom = digitalZoomFormatted,
            cropFactor = cropFactorFormatted
        )


    }

    suspend fun getAllApps() {
        withContext(Dispatchers.IO) {

            val pm = context.packageManager
            val listOfApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            } else {
                pm.getInstalledPackages(0)
            }

            val userApps = mutableListOf<PackageInfo>()
            val systemApps = mutableListOf<PackageInfo>()

            userApps.addAll(listOfApps.filter {
                it.applicationInfo.flags and 1 == 0
            })

            systemApps.addAll(listOfApps.filter {
                it.applicationInfo.flags and 1 != 0
            })

            _allApps.postValue(listOfApps)
            _userApps.postValue(userApps)
            _systemApps.postValue(systemApps)

        }
    }

}
