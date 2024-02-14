package com.androvine.deviceinfo.fragments.bottomNav

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentHomeBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.androvine.icons.AndroidVersionIcon
import com.androvine.icons.BrandIcons
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.math.log


class HomeFragment : Fragment() {


    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.systemDataModel.observe(viewLifecycleOwner){
            if (it!=null){
                val brandImage = BrandIcons().getBrandIconByName(it.brand!!)
                if (brandImage != null) {
                    binding.brandImage.setImageResource(brandImage)
                } else {
                    binding.brandImage.setImageResource(R.drawable.ic_android_partner)
                }
                binding.brandName.text = it.brand
                binding.deviceName.text = it.name
            }
        }

        deviceDetailsViewModel.osDataModel.observe(viewLifecycleOwner){
            if (it!=null){
                binding.androidVersion.text = it.versionName
                binding.androidIcon.setImageResource(AndroidVersionIcon().getVersionByApiLevel(it.apiLevel)!!.image)
            }
        }



//        val brand = Build.BRAND
//        Log.e("HomeFragment", "Brand: $brand")
//
//
//        val model = Build.MODEL
//        Log.e("HomeFragment", "Model: $model")
//
//        val device = Build.DEVICE
//        Log.e("HomeFragment", "Device: $device")
//
//        val product = Build.PRODUCT
//        Log.e("HomeFragment", "Product: $product")
//
//        val manufacturer = Build.MANUFACTURER
//        Log.e("HomeFragment", "Manufacturer: $manufacturer")
//
//        val hardware = Build.HARDWARE
//        Log.e("HomeFragment", "Hardware: $hardware")
//
//        val board = Build.BOARD
//        Log.e("HomeFragment", "Board: $board")
//
//        val bootloader = Build.BOOTLOADER
//        Log.e("HomeFragment", "Bootloader: $bootloader")
//
//        val display = Build.DISPLAY
//        Log.e("HomeFragment", "Display: $display")
//
//        val fingerprint = Build.FINGERPRINT
//        Log.e("HomeFragment", "Fingerprint: $fingerprint")
//
//        val host = Build.HOST
//        Log.e("HomeFragment", "Host: $host")
//
//        val id = Build.ID
//        Log.e("HomeFragment", "ID: $id")
//
//        val tags = Build.TAGS
//        Log.e("HomeFragment", "Tags: $tags")
//
//        val type = Build.TYPE
//        Log.e("HomeFragment", "Type: $type")
//
//        val user = Build.USER
//        Log.e("HomeFragment", "User: $user")
//
//
//        val releaseDate = Build.TIME
//        Log.e("HomeFragment", "Release Date: $releaseDate")
//
//        val sdk = Build.VERSION.SDK_INT
//        Log.e("HomeFragment", "SDK: $sdk")
//
//        val sdkVersion = Build.VERSION.SDK_INT
//        Log.e("HomeFragment", "SDK Version: $sdkVersion")
//
//        val supportedAbis = Build.SUPPORTED_ABIS
//        Log.e("HomeFragment", "Supported ABIs: $supportedAbis")
//
//        val supported32BitAbis = Build.SUPPORTED_32_BIT_ABIS
//        Log.e("HomeFragment", "Supported 32 Bit ABIs: $supported32BitAbis")
//
//        val supported64BitAbis = Build.SUPPORTED_64_BIT_ABIS
//        Log.e("HomeFragment", "Supported 64 Bit ABIs: $supported64BitAbis")
//
//
//        val incremental = Build.VERSION.INCREMENTAL
//        Log.e("HomeFragment", "Incremental: $incremental")
//
//        val release = Build.VERSION.RELEASE
//        Log.e("HomeFragment", "Release: $release")
//
//        val baseOS = Build.VERSION.BASE_OS
//        Log.e("HomeFragment", "Base OS: $baseOS")
//
//        val securityPatch = Build.VERSION.SECURITY_PATCH
//        Log.e("HomeFragment", "Security Patch: $securityPatch")
//
//        val sdkInt = Build.VERSION.SDK_INT
//        Log.e("HomeFragment", "SDK Int: $sdkInt")
//
//        val previewSdkInt = Build.VERSION.PREVIEW_SDK_INT
//        Log.e("HomeFragment", "Preview SDK Int: $previewSdkInt")
//
//        val codename = Build.VERSION.CODENAME
//        Log.e("HomeFragment", "Codename: $codename")
//
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val mediaPerformanceClass = Build.VERSION.MEDIA_PERFORMANCE_CLASS
//            Log.e("HomeFragment", "Media Performance Class: $mediaPerformanceClass")
//
//            val releaseOrCodename = Build.VERSION.RELEASE_OR_CODENAME
//            Log.e("HomeFragment", "Release or Codename: $releaseOrCodename")
//
//
//            val socManufacturer = Build.SOC_MANUFACTURER
//            Log.e("HomeFragment", "Soc Manufacturer: $socManufacturer")
//
//
//
//
//            val skuOfHardware = Build.SKU
//            Log.e("HomeFragment", "SKU of Hardware: $skuOfHardware")
//
//            val socModel = Build.SOC_MODEL
//            Log.e("HomeFragment", "Soc Model: $socModel")
//
//        }
//
//
//
//        val radioVersion = Build.getRadioVersion()
//        Log.e("HomeFragment", "Radio Version: $radioVersion")


    }


}