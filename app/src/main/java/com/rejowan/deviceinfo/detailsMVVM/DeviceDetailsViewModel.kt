package com.rejowan.deviceinfo.detailsMVVM

import android.view.Display
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeviceDetailsViewModel(private val repository: DeviceDetailsRepository) : ViewModel() {

    val systemDataModel = repository.systemDataModel
    val osDataModel = repository.osDataModel
    val cpuDataModel = repository.cpuDataModel
    val gpuDataModel = repository.gpuDataModel
    val displayDataModel = repository.displayDataModel
    val memoryDataModel = repository.memoryDataModel
    val storageDataModel = repository.storageDataModel
    val cameraDataModel = repository.cameraDataModel
    val allApps = repository.allApps
    val userApps = repository.userApps
    val systemApps = repository.systemApps


    fun copyDatabaseFromAssets() {
        viewModelScope.launch {
            repository.copyDatabaseFromAssets()
        }
    }

    fun getSystemData() {
        viewModelScope.launch {
            repository.getSystemData()
        }
    }

    fun getOsData() {
        viewModelScope.launch {
            repository.getOsData()
        }
    }

    fun getCpuData() {
        viewModelScope.launch {
            repository.getCpuData()
        }
    }

    fun getGpuData(vendor: String?, renderer: String?, version: String?, shaderVersion: String?,  extensions: String?) {
        viewModelScope.launch {
            repository.getGpuData(vendor, renderer, version, shaderVersion, extensions)
        }

    }

    fun getDisplayData(display: Display?) {
        viewModelScope.launch {
            repository.getDisplayData(display)
        }
    }

    fun getMemoryData() {
        viewModelScope.launch {
            repository.getMemoryData()
        }
    }

    fun getStorageData() {
        viewModelScope.launch {
            repository.getStorageData()
        }
    }

    fun getCameraData() {
        viewModelScope.launch {
            repository.getCameraData()
        }
    }

    fun getAllApps() {
        viewModelScope.launch {
            repository.getAllApps()
        }
    }
}