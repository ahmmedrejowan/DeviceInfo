package com.rejowan.deviceinfo.fragments.device

import android.media.MediaCodecList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rejowan.deviceinfo.adapter.NetworkDetailsListAdapter
import com.rejowan.deviceinfo.databinding.FragmentCodecsBinding
import java.util.concurrent.Executors


class CodecsFragment : Fragment() {

    private val binding by lazy { FragmentCodecsBinding.inflate(layoutInflater) }

    private val listOfDetailsPairs = mutableListOf<Pair<String, String>>()

    private val adapter: NetworkDetailsListAdapter by lazy {
        NetworkDetailsListAdapter(
            mutableListOf()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Executors.newSingleThreadExecutor().execute {
            val codecs = MediaCodecList(MediaCodecList.ALL_CODECS)
            val codecCount = codecs.codecInfos.size

            for (i in 0 until codecCount) {
                val codecInfo = codecs.codecInfos[i]
                val codecName = codecInfo.name
                val codecSupportedTypes = codecInfo.supportedTypes.joinToString(", ")
                listOfDetailsPairs.add(Pair(codecName, codecSupportedTypes))
            }

            val sortedList = listOfDetailsPairs.sortedBy { it.first }

            requireActivity().runOnUiThread {
                binding.top1.text = "$codecCount Codecs"
                adapter.updateList(sortedList)

            }
        }


    }


}