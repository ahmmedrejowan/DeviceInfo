package com.androvine.deviceinfo.fragments.device

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.databinding.FragmentCameraBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class CameraFragment : Fragment() {

    private val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.cameraDataModel.observe(viewLifecycleOwner) {
            Log.e("CameraFragment", "CameraDataModel1: ${it!!.cameraList[0]}")
            Log.e("CameraFragment", "CameraDataModel2: ${it!!.cameraList[1]}")

        }


    }


}