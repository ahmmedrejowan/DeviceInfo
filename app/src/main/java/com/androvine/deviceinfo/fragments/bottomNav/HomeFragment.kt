package com.androvine.deviceinfo.fragments.bottomNav

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var handler : Handler
    private lateinit var uptimeRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        uptimeRunnable = Runnable {
            updateUsage()
            handler.postDelayed(uptimeRunnable, 2000)
        }

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


        deviceDetailsViewModel.memoryDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                binding.arcProgressRam.progress = it.usagePercent.toFloat()


            }

        }

    }

    private fun updateUsage() {

        updateRam()


    }

    private fun updateRam() {


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

}