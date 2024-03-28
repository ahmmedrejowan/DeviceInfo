package com.rejowan.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rejowan.deviceinfo.databinding.FragmentDisplayBinding
import com.rejowan.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class DisplayFragment : Fragment() {

    private val binding by lazy { FragmentDisplayBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.displayDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.resolution.text = it.resolution + " px"
                binding.resolution2.text = it.resolution + " (WxH) pixels"
                binding.refreshRate.text = it.refreshRate
                binding.refreshRate2.text = it.refreshRate
                binding.size.text = it.size
                binding.size2.text = it.size
                binding.absoluteResolution.text = it.absoluteResolution + " (WxH) pixels"
                binding.screenDensity.text = it.screenDensity + " ppi"
                binding.densityDpi.text = it.densityDpi + " dpi"
                binding.densityScale.text = it.density
                binding.supportedRefreshRates.text = it.supportedRefreshRates
                binding.hdr.text = it.hdr
                binding.hdrCapabilities.text = it.hdrCaps
                binding.colorGamut.text = it.colorGamut
                binding.brightness.text = it.brightness
                binding.brightnessMode.text = it.brightnessMode
                binding.displayOrientation.text = it.orientation

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