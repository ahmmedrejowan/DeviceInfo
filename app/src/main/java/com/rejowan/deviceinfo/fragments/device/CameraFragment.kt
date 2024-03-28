package com.rejowan.deviceinfo.fragments.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rejowan.deviceinfo.databinding.FragmentCameraBinding
import com.rejowan.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.rejowan.deviceinfo.detailsMVVM.dataClass.MiniCameraModel
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
           if (it!=null) {
               setupBackCamera(it.cameraList[0])

               setupFrontCamera(it.cameraList[1])
           }

        }


    }

    private fun setupFrontCamera(miniCameraModel: MiniCameraModel) {
        binding.frontCameraName.text = miniCameraModel.cameraName
        binding.frontCameraMegapixels.text = miniCameraModel.megapixels
        binding.frontCameraAperture.text = miniCameraModel.aperture
        binding.frontCameraFocalLength.text = miniCameraModel.focalLength
        binding.frontCameraSensorSize.text = miniCameraModel.sensorSize
        binding.frontCameraShutterSpeed.text = miniCameraModel.shutterSpeed
        binding.frontCameraIsoRange.text = miniCameraModel.iso
        binding.frontCameraHighestResolution.text = miniCameraModel.highestResolution
        binding.frontCameraAntiBanding.text = miniCameraModel.antiBanding
        binding.frontCameraExposure.text = miniCameraModel.autoExposer
        binding.frontCameraFocus.text = miniCameraModel.autoFocus
        binding.frontCameraWhiteBalance.text = miniCameraModel.whiteBalance
        binding.frontCameraSceneMode.text = miniCameraModel.sceneMode
        binding.frontCameraFlash.text = miniCameraModel.flash
        binding.frontCameraOrientation.text = miniCameraModel.orientation
        binding.frontCameraOis.text = miniCameraModel.opticalStabilization
        binding.frontCameraVideoStabilization.text = miniCameraModel.videoStabilization
        binding.frontCameraDigitalZoom.text = miniCameraModel.digitalZoom
        binding.frontCameraCropFactor.text = miniCameraModel.cropFactor
    }

    private fun setupBackCamera(miniCameraModel: MiniCameraModel) {
        binding.backCameraName.text = miniCameraModel.cameraName
        binding.backCameraMegapixels.text = miniCameraModel.megapixels
        binding.backCameraAperture.text = miniCameraModel.aperture
        binding.backCameraFocalLength.text = miniCameraModel.focalLength
        binding.backCameraSensorSize.text = miniCameraModel.sensorSize
        binding.backCameraShutterSpeed.text = miniCameraModel.shutterSpeed
        binding.backCameraIsoRange.text = miniCameraModel.iso
        binding.backCameraHighestResolution.text = miniCameraModel.highestResolution
        binding.backCameraAntiBanding.text = miniCameraModel.antiBanding
        binding.backCameraExposure.text = miniCameraModel.autoExposer
        binding.backCameraFocus.text = miniCameraModel.autoFocus
        binding.backCameraWhiteBalance.text = miniCameraModel.whiteBalance
        binding.backCameraSceneMode.text = miniCameraModel.sceneMode
        binding.backCameraFlash.text = miniCameraModel.flash
        binding.backCameraOrientation.text = miniCameraModel.orientation
        binding.backCameraOis.text = miniCameraModel.opticalStabilization
        binding.backCameraVideoStabilization.text = miniCameraModel.videoStabilization
        binding.backCameraDigitalZoom.text = miniCameraModel.digitalZoom
        binding.backCameraCropFactor.text = miniCameraModel.cropFactor


    }


}