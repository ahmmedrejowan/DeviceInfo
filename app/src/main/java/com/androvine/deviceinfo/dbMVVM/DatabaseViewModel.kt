package com.androvine.deviceinfo.dbMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DatabaseRepository) : ViewModel() {


    fun copyDatabaseFromAssets() {
        viewModelScope.launch {
            repository.copyDatabaseFromAssets()
        }
    }

}