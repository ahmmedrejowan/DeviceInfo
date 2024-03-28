package com.rejowan.deviceinfo.ui.test

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.databinding.ActivityMicrophoneBinding
import com.rejowan.deviceinfo.utils.TestingPrefs
import java.io.File
import java.io.IOException

class Microphone : AppCompatActivity() {


    private val binding by lazy { ActivityMicrophoneBinding.inflate(layoutInflater) }

    private val testingPrefs by lazy { TestingPrefs(this) }


    private val recordAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startRecording()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (hasRecordAudioPermission()) {
            startRecording()
        } else {
            requestRecordAudioPermission()
        }



        binding.yesButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            if (mediaRecorder != null) {
                stopRecording()
            }
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.MICROPHONE.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            if (mediaRecorder != null) {
                stopRecording()
            }
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.MICROPHONE.key, false)
            finish()
        }


    }

    private fun hasRecordAudioPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestRecordAudioPermission() {
        recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(getOutputFile().absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                // Handle recording start error
            }
        }

        // Record for 5 seconds and then stop
//        binding.root.postDelayed({
//            stopRecording()
//            playRecordedAudio()
//        }, 5000)

        var count = 5
        binding.countDown.text = count.toString()
        val countdownHandler = Handler(Looper.getMainLooper())
        val countdownRunnable = object : Runnable {
            override fun run() {
                count--
                if (count >= 1) {
                    binding.countDown.text = count.toString()
                    countdownHandler.postDelayed(this, 1000) // Update countdown every second
                } else {
                    binding.countDown.visibility = android.view.View.GONE
                    binding.playingInTextView.text = "Now Playing Recorded Audio..."
                    binding.speakerImageView.setImageResource(R.drawable.baseline_volume_up_24)
                    stopRecording()
                    playRecordedAudio()
                }
            }
        }
        countdownHandler.postDelayed(countdownRunnable, 1000)


    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    private fun playRecordedAudio() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(getOutputFile().absolutePath)
                prepare()
                start()
            } catch (e: IOException) {
                // Handle playback start error
            }
        }
    }

    private fun getOutputFile(): File {
        return File(externalCacheDir, "recorded_audio.3gp")
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this).setTitle("Permission Denied")
            .setMessage("Microphone permission is required to perform this action.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }.show()
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

}