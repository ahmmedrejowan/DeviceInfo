package com.rejowan.deviceinfo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.databinding.ActivitySettingsBinding
import com.rejowan.licensy.LicenseContent
import com.rejowan.licensy.Licenses
import com.rejowan.licensy.LicensyBottomSheet
import com.rejowan.licensy.LicensyCustomization
import com.rejowan.licensy.LicensyDialog
import com.rejowan.wachatanalyzer.constants.Constants

class Settings : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingFragment()).commit()


    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.my_prefs, rootKey)

            setUpListeners()
        }

        private fun setUpListeners() {

            val darkThemePref =
                findPreference<androidx.preference.ListPreference>("pref_dark_theme")
            darkThemePref?.setOnPreferenceChangeListener { _, newValue ->
                when (newValue as String) {
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
                requireActivity().recreate()
                true
            }


            val appVersionPref = findPreference<androidx.preference.Preference>("pref_version")
            val creditPref = findPreference<androidx.preference.Preference>("pref_credit")
            val sourcePref = findPreference<androidx.preference.Preference>("pref_source")
            val createdByPref = findPreference<androidx.preference.Preference>("pref_creator")


            appVersionPref?.summary = "Version " + Constants.appVersion
            createdByPref?.summary = "" + Constants.companyName



            sourcePref?.setOnPreferenceClickListener {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(Constants.sourceCode)
                )
                startActivity(browserIntent)
                true
            }

            createdByPref?.setOnPreferenceClickListener {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(Constants.profileUrl)
                )
                startActivity(browserIntent)
                true
            }

            val listOfLicenses = mutableListOf<LicenseContent>()
            // lottie
            listOfLicenses.add(
                LicenseContent(
                    "Lottie",
                    "Airbnb",
                    Licenses.APACHE_2_0,
                    null,
                    "https://github.com/airbnb/lottie-android"
                )
            )

            // koin
            listOfLicenses.add(
                LicenseContent(
                    "Koin",
                    "InsertKoinIO",
                    Licenses.APACHE_2_0,
                    "2017",
                    "https://github.com/InsertKoinIO/koin"
                )
            )

            // MP Android Chart
            listOfLicenses.add(
                LicenseContent(
                    "MP Android Chart",
                    "PhilJay",
                    Licenses.APACHE_2_0,
                    "2020",
                    "https://github.com/PhilJay/MPAndroidChart"
                )
            )

            // Android Battery View
            listOfLicenses.add(
                LicenseContent(
                    "Android Battery View",
                    "ahmmedrejowan",
                    Licenses.APACHE_2_0,
                    "2024",
                    "https://github.com/ahmmedrejowan/AndroidBatteryView"
                )
            )

            // Glide
            listOfLicenses.add(
                LicenseContent(
                    "Glide",
                    "bumptech",
                    Licenses.BSD_2_CLAUSE,
                    "2014",
                    "https://github.com/bumptech/glide"
                )
            )

            // Licensy
            listOfLicenses.add(
                LicenseContent(
                    "Licensy",
                    "ahmmedrejowan",
                    Licenses.APACHE_2_0,
                    "2024",
                    "https://github.com/ahmmedrejowan/Licensy"
                )
            )

            val dialog = LicensyDialog(requireContext())
            dialog.setAccentColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            dialog.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.backgroundColor))
            dialog.setTitle("Open Source Licenses", ContextCompat.getColor(requireContext(), R.color.colorPrimary), 20f, Typeface.BOLD)
            dialog.setCloseText("Close", ContextCompat.getColor(requireContext(), R.color.white), 14f, Typeface.NORMAL)
            dialog.setCustomization(
                LicensyCustomization(
                    lvPrimaryColor = ContextCompat.getColor(requireContext(), R.color.textColor),
                    lvSecondaryColor = ContextCompat.getColor(requireContext(), R.color.textColorSecondary),
                    lvBackgroundColor = ContextCompat.getColor(requireContext(), R.color.backgroundColor),
                    lvDividerColor = ContextCompat.getColor(requireContext(), R.color.grey),
                    lvBackgroundColorExpand = ContextCompat.getColor(requireContext(), R.color.backgroundColorSecondary),
                )
            )
            dialog.setLicenses(listOfLicenses)

            creditPref?.setOnPreferenceClickListener {

                dialog.show()

                true
            }


        }


    }

}