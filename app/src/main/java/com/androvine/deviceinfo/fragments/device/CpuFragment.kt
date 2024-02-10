package com.androvine.deviceinfo.fragments.device

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.databinding.FragmentCpuBinding
import com.androvine.deviceinfo.databinding.FragmentOsBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class CpuFragment : Fragment() {

    private val binding by lazy { FragmentCpuBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.cpuDataModel.observe(viewLifecycleOwner) {
            if (it != null) {

                when (it.manufacturer) {
                    "Qualcomm®" -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_snapdragon)
                    }

                    "Mediatek" -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_mediatek)
                    }

                    "Samsung" -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_exynos)
                    }

                    "Google" -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_tensor)
                    }

                    "HiSilicon" -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_hiisilicon)
                    }

                    else -> {
                        binding.brandIcon.setImageResource(R.drawable.ic_processor_generic)
                    }
                }


                binding.vendorTop.text = it.manufacturer
                binding.marketNameTop.text = it.name
                binding.modelName.text = it.model
                binding.marketName.text = it.name
                binding.vendor.text = it.manufacturer
                binding.architecture.text = it.architecture
                binding.fabrication.text = it.fab
                binding.coreCount.text = it.coreCount
                binding.coreDetails.text = it.coreDetail
                binding.maxFreq.text = it.frequency
                binding.governor.text = it.governor
                binding.cpuBits.text = it.cpuBit
                binding.features.text = it.cpuFeatures
                binding.implementer.text = it.cpuImplementer
                binding.part.text = it.cpuPart
                binding.revision.text = it.cpuRevision
                binding.variant.text = it.cpuVariant


            }
        }


    }


}