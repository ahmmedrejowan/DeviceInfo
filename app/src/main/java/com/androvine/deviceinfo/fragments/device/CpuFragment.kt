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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceDetailsViewModel.cpuDataModel.observe(viewLifecycleOwner) {
           Log.e("CpuFragment", "CpuDataModel: $it")
        }
    }

}