package com.androvine.deviceinfo.detailsMVVM.dataClass

data class StorageDataModel (
    val totalSize : String,
    val usedSize : String,
    val freeSize : String,
    val appUsedSize : String,
    val systemUsedSize : String,
    val storageType : String,
    val internalStorageFileSystem : String,
    val internalStorageBlockSize : String,
    val internalStoragePartition: String,
    val internalStorageSize : String,
    val internalStorageUsedSize : String,
    val internalStorageFreeSize : String,
    val isExternalStorageAvailable : Boolean,
    val externalStorageFileSystem : String,
    val externalStorageBlockSize : String,
    val externalStoragePartition: String,
    val externalStorageSize : String,
    val externalStorageUsedSize : String,
    val externalStorageFreeSize : String

)