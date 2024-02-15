package com.androvine.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androvine.deviceinfo.databinding.FragmentRamBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class RamFragment : Fragment() {

    private val binding by lazy { FragmentRamBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    private lateinit var handler : Handler
    private lateinit var uptimeRunnable: Runnable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        uptimeRunnable = Runnable {
            updateUsage()
            handler.postDelayed(uptimeRunnable, 2000)
        }

        deviceDetailsViewModel.memoryDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.advertisedRamSize.text = it.advertisedMemory
                binding.advertisedRamSize2.text = it.advertisedMemory
                binding.progressBar.progress = it.usagePercent
                binding.usage.text = it.usagePercent.toString() + "%"
                binding.usedRam.text = it.usedMemory
                binding.availableRam.text = it.totalMemory
                binding.freeRam.text = it.availableMemory
                binding.threshold.text = it.threshold
                binding.isLowMemory.text = it.lowMemory

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

    private fun updateUsage() {

        deviceDetailsViewModel.getMemoryData()

    }

}