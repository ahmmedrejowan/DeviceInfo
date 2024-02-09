package com.androvine.deviceinfo.detailsMVVM

import android.content.Context
import android.content.pm.PackageManager
import com.google.android.gms.common.GoogleApiAvailability
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale

class DeviceDetailsUtils {

    companion object {


        fun getOpenGLES(context: Context): String {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            val configurationInfo = activityManager.deviceConfigurationInfo

            return configurationInfo.glEsVersion
        }

        fun isSeamlessUpdateSupported(context: Context): Boolean {
            return context.packageManager.hasSystemFeature("android.software.ota_updates_ab")
        }

        fun isTrebleSupported(): Boolean {
            return try {
                val trebleEnabled = System.getProperty("ro.treble.enabled")
                trebleEnabled != null && trebleEnabled.toBoolean()
            } catch (e: Exception) {
                false
            }
        }

        fun isTrebleEnabled(): Boolean {
            return try {
                val controlPrivAppPermissions = System.getProperty("ro.control_privapp_permissions")
                controlPrivAppPermissions != null && controlPrivAppPermissions.toBoolean()
            } catch (e: Exception) {
                false
            }
        }

        fun getFormattedUptime(uptimeMillis: Long): String? {
            // 1 day 2 hours 3 minutes 4 seconds
            val days = uptimeMillis / (1000 * 60 * 60 * 24)
            val hours = uptimeMillis / (1000 * 60 * 60) % 24
            val minutes = uptimeMillis / (1000 * 60) % 60
            val seconds = uptimeMillis / 1000 % 60
            return "$days days $hours hours $minutes minutes $seconds seconds"
        }


        fun getBuildDateFormatted(time: Long): String? {
            // 10:10:10 AM Sunday, 1 January 2000
            val date = java.util.Date(time)
            val format = SimpleDateFormat("HH:mm:ss a EEEE, d MMMM yyyy", Locale.getDefault())
            return format.format(date)
        }

        fun getSecurityPatchFormatted(securityPatch: String): String {
            val parts = securityPatch.split("-")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            return "$day $month $year"

        }

        fun isDeviceRooted(): Boolean {
            return try {
                // Check for the existence of known root files or directories
                val knownRootPaths = arrayListOf(
                    "/system/app/Superuser.apk",
                    "/sbin/su",
                    "/system/bin/su",
                    "/system/xbin/su",
                    "/data/local/xbin/su",
                    "/data/local/bin/su",
                    "/system/sd/xbin/su",
                    "/system/bin/failsafe/su",
                    "/data/local/su",
                    "/su/bin/su"
                )

                for (path in knownRootPaths) {
                    if (File(path).exists()) {
                        return true
                    }
                }

                // Check for the presence of Superuser application
                var process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                val superUserLine = bufferedReader.readLine()
                if (superUserLine != null) {
                    return true
                }

                // Execute a command using su to determine if the device is rooted
                process = Runtime.getRuntime().exec("su")
                process.outputStream.close()
                process.errorStream.close()
                process.waitFor()
                return process.exitValue() == 0
            } catch (e: Exception) {
                false
            }
        }

         fun getGooglePlayServicesVersion(context: Context): String {
            return try {
                val packageName = GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE
                val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
                packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                "N/A"
            }
        }



    }
}