package com.rejowan.deviceinfo.detailsMVVM.dataClass

data class CpuDataModel(
    val model: String,
    val name: String,
    val manufacturer: String,
    val architecture: String,
    val fab: String,
    val coreCount: String,
    val coreDetail: String,
    val frequency: String,
    val governor: String,
    val cpuBit: String,
    val cpuFeatures: String,
    val cpuImplementer: String,
    val cpuPart: String,
    val cpuRevision: String,
    val cpuVariant: String,
    val procInfo: List<ProcModel>,
)

data class ProcModel(
    val processorNumber: Int,
    val bogoMIPS: String,
    val features: String,
    val implementer: String,
    val architecture: String,
    val variant: String,
    val part: String,
    val revision: String,
)