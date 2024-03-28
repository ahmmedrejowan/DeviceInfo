package com.rejowan.deviceinfo.detailsMVVM.dataClass

data class MemoryDataModel(
    val advertisedMemory: String,
    val totalMemory: String,
    val availableMemory: String,
    val usedMemory: String,
    val usagePercent: Int,
    val threshold: String,
    val lowMemory: String
)