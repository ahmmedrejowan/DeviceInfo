package com.androvine.deviceinfo.detailsMVVM

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val deviceDetailsModule = module {
    single { DeviceDetailsRepository(androidContext()) }
    viewModel { DeviceDetailsViewModel(get()) }
}