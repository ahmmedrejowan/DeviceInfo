package com.androvine.deviceinfo.dataClasses

data class CpuDBModel(
    val model: String,
    val name: String,
    val fab: String,
    val gpu: String,
    val core: String,
    val vendor: String
)

