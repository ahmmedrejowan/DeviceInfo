package com.androvine.deviceinfo.detailsMVVM

import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.androvine.deviceinfo.dataClasses.CpuDataModel
import com.androvine.deviceinfo.dataClasses.DeviceDataModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import com.androvine.deviceinfo.detailsMVVM.dataClass.SystemDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DeviceDetailsRepository(private val context: Context) {

    private val cpuDatabaseHelper = CpuDatabaseHelper(context)
    private val deviceDatabaseHelper = DeviceDatabaseHelper(context)

    private val _systemDataModel = MutableLiveData<SystemDataModel?>()
    val systemDataModel get() = _systemDataModel

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
            if (name== null){
                name= Build.MODEL
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
                bootloader = Build.BOOTLOADER,
                radio = Build.getRadioVersion()
            )
            _systemDataModel.postValue(systemData)
        }

    }

}