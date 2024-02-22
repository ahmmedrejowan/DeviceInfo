package com.androvine.deviceinfo.fragments.device

import android.content.Context
import android.media.MediaCodecList
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.NetworkDetailsListAdapter
import com.androvine.deviceinfo.databinding.FragmentCodecsBinding
import com.androvine.deviceinfo.databinding.FragmentConnectionBinding


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

        val codecs = MediaCodecList(MediaCodecList.ALL_CODECS)
        val codecCount = codecs.codecInfos.size

        binding.top1.text = "$codecCount Codecs"

        for (i in 0 until codecCount) {
            val codecInfo = codecs.codecInfos[i]
            val codecName = codecInfo.name
            val codecSupportedTypes = codecInfo.supportedTypes.joinToString(", ")
            listOfDetailsPairs.add(Pair(codecName, codecSupportedTypes))
        }

        val sortedList = listOfDetailsPairs.sortedBy { it.first }
        adapter.updateList(sortedList)

    }

}