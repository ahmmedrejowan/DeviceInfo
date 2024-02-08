package com.androvine.deviceinfo.dbMVVM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androvine.deviceinfo.dataClasses.CpuDataModel
import com.androvine.deviceinfo.dataClasses.DeviceDataModel
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(private val context: Context) {

    private val cpuDatabaseHelper = CpuDatabaseHelper(context)
    private val deviceDatabaseHelper = DeviceDatabaseHelper(context)

    private val _cpuDataModel = MutableLiveData<CpuDataModel?>()
    val cpuDataModel: LiveData<CpuDataModel?> get() = _cpuDataModel

    private val _deviceDataModel = MutableLiveData<DeviceDataModel?>()
    val deviceDataModel: LiveData<DeviceDataModel?> get() = _deviceDataModel


    suspend fun copyDatabaseFromAssets() {

        withContext(Dispatchers.IO) {
            cpuDatabaseHelper.copyDatabaseFromAssets(context)
            deviceDatabaseHelper.copyDatabaseFromAssets(context)
        }

    }

    suspend fun getCpuDataByModel(model: String) {
        withContext(Dispatchers.IO) {
            val cpuData = cpuDatabaseHelper.getCpuDataByModel(model)
            _cpuDataModel.postValue(cpuData)
        }
    }

    suspend fun getDeviceDataByModel(model: String) {
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByModel(model)
            _deviceDataModel.postValue(deviceData)
        }
    }

    suspend fun getDeviceDataByDevice(device: String) {
        withContext(Dispatchers.IO) {
            val deviceData = deviceDatabaseHelper.getDeviceByDevice(device)
            _deviceDataModel.postValue(deviceData)
        }
    }

    suspend fun getAllBrandList() {
        return withContext(Dispatchers.IO) {
          val brands =  deviceDatabaseHelper.getAllBrand()
            for (brand in brands) {
                Log.e("TAG", "Brand: " + brand)
            }
        }
    }



}