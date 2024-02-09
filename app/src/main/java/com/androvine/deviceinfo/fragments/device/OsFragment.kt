package com.androvine.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.databinding.FragmentOsBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils.Companion.getFormattedUptime
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.androvine.icons.AndroidVersionIcon
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class OsFragment : Fragment() {

    private val binding by lazy { FragmentOsBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    private lateinit var handler : Handler
    private lateinit var uptimeRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        uptimeRunnable = Runnable {
            updateUpTime()
            handler.postDelayed(uptimeRunnable, 1000)
        }

        deviceDetailsViewModel.osDataModel.observe(viewLifecycleOwner) {
            if (it != null) {
                val androidVersionModel = AndroidVersionIcon().getVersionByApiLevel(it.apiLevel)

                binding.osIcon.setImageResource(androidVersionModel!!.image)
                binding.versionName.text = it.versionName.toString()
                binding.apiLevelName.text = "API " + it.apiLevel.toString()

                binding.apiLevel.text = it.apiLevel.toString()
                binding.version.text = it.version.toString()
                binding.versionNameDetail.text = it.versionName.toString()
                binding.codeName.text = it.codeName.toString()
                binding.bootloader.text = it.bootloader.toString()
                binding.securityPatchLevel.text = it.securityPatch.toString()
                binding.buildNumber.text = it.build.toString()
                binding.buildDate.text = it.buildDate.toString()
                binding.fingerprint.text = it.fingerprint.toString()
                binding.architecture.text = it.architecture.toString()
                binding.abis.text = listToString(it.abis)
                binding.abis32.text = listToString(it.abis32)
                binding.abis64.text = listToString(it.abis64)
                binding.kernelVersion.text = it.kernelVersion.toString()
                binding.rootAccess.text =
                    if (it.isRooted) "Device is Rooted" else "Device is not Rooted"
                binding.javaVMName.text = it.javaVm.toString()
                binding.javaVMVersion.text = it.javaVmVersion.toString()
                binding.incrementalVersion.text = it.incremental.toString()
                binding.googlePlayServicesVersion.text = it.googlePlayServicesVersion.toString()
                binding.timeZone.text = it.timeZone.toString()
                binding.openGLVersion.text = it.openGlVersion.toString()
                binding.trebleSupport.text =
                    if (it.trebleSupported) "Supported" else "Not Supported"
                binding.seamlessSystemUpdates.text =
                    if (it.seamlessSupported) "Supported" else "Not Supported"


            }
        }


        binding.parentLayout.setOnTouchListener { _, event ->
            // Check if the touch event is outside the TextViews
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Clear text selection when the user touches outside
                clearTextSelection(view)
            }
            false
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(uptimeRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.post(uptimeRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(uptimeRunnable)
    }

    private fun updateUpTime() {
        val uptime = getFormattedUptime(android.os.SystemClock.uptimeMillis())
        binding.uptime.text = uptime
    }

    private fun listToString(abis: List<String>?): String {
        return if (abis != null) {
            var result = ""
            for (abi in abis) {
                result += if (abi == abis.last()) abi
                else "$abi, "
            }
            result
        } else {
            "N/A"
        }

    }

    private fun clearTextSelection(view: View) {
        // Iterate through all TextViews and clear text selection
        clearTextSelectionInView(view)
    }

    private fun clearTextSelectionInView(view: View) {
        if (view is TextView) {
            view.clearFocus()
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                clearTextSelectionInView(view.getChildAt(i))
            }
        }
    }

}