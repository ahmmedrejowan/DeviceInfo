package com.androvine.deviceinfo.detailsMVVM.dataClass

data class GpuDataModel(
    val name: String,
    val vendor: String,
    val renderer: String,
    val version: String,
    val glEsVersion: String,
    val shaderVersion: String,
    val extensions: String,
)
