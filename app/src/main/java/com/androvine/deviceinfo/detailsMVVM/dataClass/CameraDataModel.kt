package com.androvine.deviceinfo.detailsMVVM.dataClass

data class CameraDataModel(
    val cameraList: List<MiniCameraModel>
)

data class MiniCameraModel(
    val cameraName: String,
    val megapixels: String,
    val aperture: String,
    val focalLength: String,
    val sensorSize: String,
    val shutterSpeed: String,
    val iso: String,
    val highestResolution: String,
    val pixelSize: String,
    val antiBanding: String,
    val autoExposer: String,
    val autoFocus: String,
    val whiteBalance: String,
    val sceneMode: String,
    val flash: String,
    val orientation: String,
    val opticalStabilization: String,
)
