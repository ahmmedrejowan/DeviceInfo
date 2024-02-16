package com.androvine.deviceinfo.detailsMVVM.dataClass

data class StorageDataModel(
    val totalSize: Long,
    val usedSize: Long,
    val freeSize: Long,
    val list: List<MiniStorageModel>
)

data class MiniStorageModel(
    val totalSize: Long,
    val usedSize: Long,
    val freeSize: Long,
    val storageName: String,
    val storagePath: String
)