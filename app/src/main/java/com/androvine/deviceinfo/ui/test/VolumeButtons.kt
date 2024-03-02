package com.androvine.deviceinfo.ui.test

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.androvine.deviceinfo.databinding.ActivityVolumeButtonsBinding
import com.androvine.deviceinfo.utils.TestingPrefs

class VolumeButtons : AppCompatActivity() {

    private val binding by lazy { ActivityVolumeButtonsBinding.inflate(layoutInflater) }

    private val testingPrefs by lazy { TestingPrefs(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.yesButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.VOLUME.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.VOLUME.key, false)
            finish()
        }


    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    binding.details.text = "Volume Up pressed"
                    return true
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    binding.details.text = "Volume Down pressed"
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }


}