package com.androvine.deviceinfo.ui.test

import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.ActivityFlashlightBinding
import com.androvine.deviceinfo.databinding.ActivitySpeakerBinding
import com.androvine.deviceinfo.utils.TestingPrefs

class Speaker : AppCompatActivity() {

    private val binding by lazy { ActivitySpeakerBinding.inflate(layoutInflater) }

    private val testingPrefs by lazy { TestingPrefs(this) }

    private lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.test_sound)
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("Speaker", "Error: $e")
        }

        binding.yesButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.SPEAKER.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.SPEAKER.key, false)
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }


}