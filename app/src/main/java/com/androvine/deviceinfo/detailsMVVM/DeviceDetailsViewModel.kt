package com.androvine.deviceinfo.detailsMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeviceDetailsViewModel(private val repository: DeviceDetailsRepository) : ViewModel() {

    val systemDataModel = repository.systemDataModel

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
}