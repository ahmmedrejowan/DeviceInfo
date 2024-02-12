package com.androvine.deviceinfo.detailsMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeviceDetailsViewModel(private val repository: DeviceDetailsRepository) : ViewModel() {

    val systemDataModel = repository.systemDataModel
    val osDataModel = repository.osDataModel
    val cpuDataModel = repository.cpuDataModel
    val gpuDataModel = repository.gpuDataModel

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
}