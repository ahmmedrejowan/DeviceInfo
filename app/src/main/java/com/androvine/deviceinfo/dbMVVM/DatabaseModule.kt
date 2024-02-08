package com.androvine.deviceinfo.dbMVVM

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseRepository(androidContext()) }
    viewModel { DatabaseViewModel(get()) }
}