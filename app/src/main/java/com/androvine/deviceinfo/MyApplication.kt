package com.androvine.deviceinfo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()


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