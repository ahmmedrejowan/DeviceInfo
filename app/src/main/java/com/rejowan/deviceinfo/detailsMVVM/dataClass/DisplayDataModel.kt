package com.rejowan.deviceinfo.detailsMVVM.dataClass

data class DisplayDataModel(
    val resolution: String,
    val screenDensity: String,
    val density: String,
    val densityDpi: String,
    val size: String,
    val aspectRatio: String,
    val refreshRate: String,
    val supportedRefreshRates: String,
    val hdr: String,
    val hdrCaps: String,
    val colorGamut: String,
    val absoluteResolution: String,
    val brightness: String,
    val brightnessMode: String,
    val orientation: String
)