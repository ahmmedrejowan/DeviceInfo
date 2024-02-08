package com.androvine.deviceinfo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.androvine.deviceinfo.detailsMVVM.deviceDetailsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(deviceDetailsModule))
        }

        // set app dark or light mode based on user preference
        val themeMode = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("pref_dark_theme", "system_default")


        when (themeMode) {
            "light_theme" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            "dark_theme" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

    }

}