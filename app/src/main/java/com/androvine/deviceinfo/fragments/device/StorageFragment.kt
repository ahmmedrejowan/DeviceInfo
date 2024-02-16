package com.androvine.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.adapter.StorageListAdapter
import com.androvine.deviceinfo.databinding.FragmentStorageBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsUtils
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class StorageFragment : Fragment() {

    private val binding by lazy { FragmentStorageBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    private lateinit var handler: Handler
    private lateinit var uptimeRunnable: Runnable
    private lateinit var storageListAdapter: StorageListAdapter


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
        storageListAdapter = StorageListAdapter(mutableListOf())
        binding.storageRecyclerView.adapter = storageListAdapter
        binding.storageRecyclerView.setHasFixedSize(true)
        binding.storageRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        deviceDetailsViewModel.storageDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.totalSize.text = DeviceDetailsUtils.bytesToHuman(it.totalSize)
                binding.usedSize.text = DeviceDetailsUtils.bytesToHuman(it.usedSize) + " used"
                val usedPercentage =
                    ((it.usedSize.toFloat() / it.totalSize.toFloat()) * 100).toInt()
                binding.progressBar.progress = usedPercentage
                binding.usagePercent.text = "$usedPercentage%"

                binding.romSize.text = DeviceDetailsUtils.bytesToHuman(it.totalSize)
                binding.freeRom.text = DeviceDetailsUtils.bytesToHuman(it.freeSize)
                binding.usedRom.text = DeviceDetailsUtils.bytesToHuman(it.usedSize)

                storageListAdapter.updateList(it.list)

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