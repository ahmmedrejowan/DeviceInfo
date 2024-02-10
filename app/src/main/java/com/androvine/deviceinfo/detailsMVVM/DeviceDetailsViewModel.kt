package com.androvine.deviceinfo.detailsMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeviceDetailsViewModel(private val repository: DeviceDetailsRepository) : ViewModel() {

    val systemDataModel = repository.systemDataModel
    val osDataModel = repository.osDataModel
    val cpuDataModel = repository.cpuDataModel

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
}