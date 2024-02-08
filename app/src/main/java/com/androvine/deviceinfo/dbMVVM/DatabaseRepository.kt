package com.androvine.deviceinfo.dbMVVM

import android.content.Context
import com.androvine.deviceinfo.databases.CpuDatabaseHelper
import com.androvine.deviceinfo.databases.DeviceDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(private val context: Context) {

    private val cpuDatabaseHelper = CpuDatabaseHelper(context)
    private val deviceDatabaseHelper = DeviceDatabaseHelper(context)

    suspend fun copyDatabaseFromAssets() {

        withContext(Dispatchers.IO) {
            cpuDatabaseHelper.copyDatabaseFromAssets(context)
            deviceDatabaseHelper.copyDatabaseFromAssets(context)
        }

    }


}