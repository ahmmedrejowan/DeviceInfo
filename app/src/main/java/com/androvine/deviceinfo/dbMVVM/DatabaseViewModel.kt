package com.androvine.deviceinfo.dbMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DatabaseRepository) : ViewModel() {

    val cpuDataModel = repository.cpuDataModel
    val deviceDataModel = repository.deviceDataModel

    fun copyDatabaseFromAssets() {
        viewModelScope.launch {
            repository.copyDatabaseFromAssets()
        }
    }

    fun getCpuDataByModel(model: String) {
        viewModelScope.launch {
            repository.getCpuDataByModel(model)
        }
    }

    fun getDeviceDataByModel(model: String) {
        viewModelScope.launch {
            repository.getDeviceDataByModel(model)
        }
    }

    fun getDeviceDataByDevice(device: String) {
        viewModelScope.launch {
            repository.getDeviceDataByDevice(device)
        }
    }



}