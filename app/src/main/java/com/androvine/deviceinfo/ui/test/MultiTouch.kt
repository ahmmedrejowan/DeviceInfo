package com.androvine.deviceinfo.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.content.ContentProviderCompat.requireContext
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.ActivityMultiTouchBinding
import com.androvine.deviceinfo.utils.TestingPrefs

class MultiTouch : AppCompatActivity() {

    private val binding by lazy { ActivityMultiTouchBinding.inflate(layoutInflater) }
    private val testingPrefs by lazy { TestingPrefs(this) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.yesButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.MULTI_TOUCH.key, true)
            finish()
        }

        binding.noButton.setOnClickListener {
            testingPrefs.setTestingPrefs(TestingPrefs.TestingPrefsKey.MULTI_TOUCH.key, false)
            finish()
        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointersCount = event.pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> binding.touchCountTextView.text = "Finger Count: 0"
            else -> binding.touchCountTextView.text = "Finger Count: $pointersCount"
        }

        return true
    }


}