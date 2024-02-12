package com.androvine.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentGpuBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class GpuFragment : Fragment() {

    private val binding by lazy { FragmentGpuBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.gpuDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.name.text = it.name
                binding.name2.text = it.name
                binding.vendor.text = it.vendor
                binding.openGlVersion.text = it.glEsVersion
                binding.renderer.text = it.renderer
                binding.shaderLanguageVersion.text = it.shaderVersion
                binding.version.text = it.version


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