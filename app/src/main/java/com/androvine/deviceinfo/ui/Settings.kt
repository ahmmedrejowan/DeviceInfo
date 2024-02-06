package com.androvine.deviceinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

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


        }


    }

}