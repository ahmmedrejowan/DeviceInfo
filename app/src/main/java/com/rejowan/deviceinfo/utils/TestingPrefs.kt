package com.rejowan.deviceinfo.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class TestingPrefs(context: Context) {

    companion object {
        private const val PREF_NAME = "TestingPrefs"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, 0)

    fun setTestingPrefs(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getTestingPrefs(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clearTestingPrefs() {
        sharedPreferences.edit().clear().apply()
    }

    fun removeTestingPrefs(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun getAllTestingPrefs(): Map<String, Boolean> {
        return sharedPreferences.all as Map<String, Boolean>
    }

    enum class TestingPrefsKey(val key: String) {
        MULTI_TOUCH("multi_touch"),
        FLASH("flash"),
        SPEAKER("speaker"),
        MICROPHONE("microphone"),
        PROXIMITY("proximity"),
        ACCELEROMETER("accelerometer"),
        LIGHT_SENSOR("light_sensor"),
        VIBRATION("vibration"),
        VOLUME("volume")
    }

}