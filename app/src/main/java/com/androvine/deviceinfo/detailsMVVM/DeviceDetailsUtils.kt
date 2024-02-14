package com.androvine.deviceinfo.detailsMVVM

import android.content.Context
import android.content.pm.PackageManager
import android.view.Display
import com.androvine.deviceinfo.detailsMVVM.dataClass.ProcModel
import com.google.android.gms.common.GoogleApiAvailability
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Math.sqrt
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

        fun getCpuMaxFrequency(): Long {
            val cpuFreqDir = File("/sys/devices/system/cpu/")
            val cpuFreqFiles = cpuFreqDir.listFiles { file -> file.isDirectory && file.name.startsWith("cpu") }

            var maxFrequency = Long.MIN_VALUE

            cpuFreqFiles?.forEach { cpuDir ->
                val maxFreqFile = File(cpuDir, "cpufreq/cpuinfo_max_freq")
                if (maxFreqFile.exists()) {
                    val maxFreqString = maxFreqFile.readText().trim()
                    val currentFrequency = maxFreqString.toLong()
                    if (currentFrequency > maxFrequency) {
                        maxFrequency = currentFrequency
                    }
                }
            }

            return if (maxFrequency != Long.MIN_VALUE) {
                // Convert to MHz
                maxFrequency / 1000
            } else {
                // Return a default value if no frequency is found
                -1
            }
        }

        fun getCPUGovernor(): String {
            return try {
                val governorFile = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
                governorFile.readText().trim()
            } catch (e: Exception) {
                "N/A"
            }
        }

//        fun parseProcModels(input: String): List<ProcModel> {
//            val procModels = mutableListOf<ProcModel>()
//
//            val processorSections = input.trim().split("\n\n")
//            for (section in processorSections.dropLast(1)) {
//                val lines = section.trim().split("\n")
//                val processorNumber = lines[0].trim().split(":")[1].trim().toInt()
//                val bogoMIPS = lines[1].trim().split(":")[1].trim()
//                val features = lines[2].trim().split(":")[1].trim()
//                val implementer = lines[3].trim().split(":")[1].trim()
//                val architecture = lines[4].trim().split(":")[1].trim()
//                val variant = lines[5].trim().split(":")[1].trim()
//                val part = lines[6].trim().split(":")[1].trim()
//                val revision = lines[7].trim().split(":")[1].trim()
//
//                Log.e(
//                    "TAG",
//                    "processorSections: " + processorNumber + " " + bogoMIPS + " " + features + " " + implementer + " " + architecture + " " + variant + " " + part + " " + revision
//                )
//
//                val procModel = ProcModel(
//                    processorNumber,
//                    bogoMIPS,
//                    features,
//                    implementer,
//                    architecture,
//                    variant,
//                    part,
//                    revision
//                )
//                procModels.add(procModel)
//            }
//
//            return procModels
//        }

        fun parseProcModels(input: String): List<ProcModel> {
            val procModels = mutableListOf<ProcModel>()

            val processorSections = input.trim().split("\n\n")
            for (section in processorSections) {
                val lines = section.trim().split("\n")
                var processorNumber = -1
                var bogoMIPS = ""
                var features = ""
                var implementer = ""
                var architecture = ""
                var variant = ""
                var part = ""
                var revision = ""

                for (line in lines) {
                    val tokens = line.trim().split(":")
                    if (tokens.size == 2) {
                        val key = tokens[0].trim()
                        val value = tokens[1].trim()

                        when (key) {
                            "processor" -> processorNumber = value.toInt()
                            "BogoMIPS" -> bogoMIPS = value
                            "Features" -> features = value
                            "CPU implementer" -> implementer = value
                            "CPU architecture" -> architecture = value
                            "CPU variant" -> variant = value
                            "CPU part" -> part = value
                            "CPU revision" -> revision = value
                        }
                    }
                }

                if (processorNumber != -1) {
                    val procModel = ProcModel(
                        processorNumber,
                        bogoMIPS,
                        features,
                        implementer,
                        architecture,
                        variant,
                        part,
                        revision
                    )
                    procModels.add(procModel)
                }
            }

            return procModels
        }


        fun getFormattedUptime(uptimeMillis: Long): String {
            val days = uptimeMillis / (1000 * 60 * 60 * 24)
            val hours = uptimeMillis / (1000 * 60 * 60) % 24
            val minutes = uptimeMillis / (1000 * 60) % 60
            val seconds = uptimeMillis / 1000 % 60
            return when {
                days > 0 -> "$days days $hours hours $minutes minutes $seconds seconds"
                hours > 0 -> "$hours hours $minutes minutes $seconds seconds"
                minutes > 0 -> "$minutes minutes $seconds seconds"
                else -> "$seconds seconds"
            }
        }


        fun getBuildDateFormatted(time: Long): String? {
            // 10:10:10 AM Sunday, 1 January 2000
            val date = java.util.Date(time)
            val format = SimpleDateFormat("hh:mm:ss a EEEE, d MMMM yyyy", Locale.getDefault())
            return format.format(date)
        }

        fun getSecurityPatchFormatted(securityPatch: String): String {
            val parts = securityPatch.split("-")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]

            // month number to name
            val monthName = when (month) {
                "01" -> "January"
                "02" -> "February"
                "03" -> "March"
                "04" -> "April"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "August"
                "09" -> "September"
                "10" -> "October"
                "11" -> "November"
                "12" -> "December"
                else -> "N/A"
            }

            return "$day $monthName $year"

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


        @Suppress("DEPRECATION")
        fun getHDRCapabilities(display: Display): String {
            val ints = display.hdrCapabilities.supportedHdrTypes
            val hdrString = StringBuilder()
            for (i in ints.indices) {
                hdrString.append(getHDRType(ints[i]))
                if (i < ints.size - 1) hdrString.append(", ")
            }
            return hdrString.toString()
        }

        fun getHDRType(type: Int): String {
            return when (type) {
                1 -> "Dolby Vision"
                2 -> "HDR10"
                3 -> "Hybrid Log-Gamma"
                4 -> "HDR10+"
                else -> "N/A"
            }
        }


        fun calculateAspectRatio(width: Int, height: Int): String {
            // Find the greatest common divisor
            fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

            // Calculate the gcd of width and height
            val divisor = gcd(width, height)

            // Calculate aspect ratio
            val aspectWidth = width / divisor
            val aspectHeight = height / divisor

            // Return aspect ratio as a string
            return "$aspectWidth:$aspectHeight"
        }


        fun calculateScreenSizeInches(widthPixels: Int, heightPixels: Int, screenDPI: Int): String {
            // Calculate the diagonal screen size in inches
            val widthInches = widthPixels / screenDPI.toDouble()
            val heightInches = heightPixels / screenDPI.toDouble()

            val screenSizeInches = kotlin.math.sqrt((widthInches * widthInches) + (heightInches * heightInches))

            // 2 decimal places
            return String.format("%.2f", screenSizeInches)

        }

    }
}