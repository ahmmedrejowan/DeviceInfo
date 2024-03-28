package com.rejowan.deviceinfo.fragments.device

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.adapter.NetworkDetailsListAdapter
import com.rejowan.deviceinfo.databinding.FragmentGsmBinding


class GsmFragment : Fragment() {

    private val binding by lazy {
        FragmentGsmBinding.inflate(layoutInflater)
    }

    private val permission = Manifest.permission.READ_PHONE_STATE
    private val listOfDetailsPairs = mutableListOf<Pair<String, String>>()
    private val adapter: NetworkDetailsListAdapter by lazy {
        NetworkDetailsListAdapter(
            mutableListOf()
        )
    }


    private val permissionIntentLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getGsmInfo()
            } else {
                binding.top1.text = "Permission Denied"
                binding.top1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.textColor
                    )
                )
                binding.top2.text = "Please grant permission"
                binding.top2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.colorPrimary
                    )
                )
                binding.top2.setOnClickListener {
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(permission), 101
                    )
                }
            }
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
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(), androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false
        )

        checkPermission()

    }

    private fun checkPermission() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(), permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.top1.text = "Permission Required"
            binding.top1.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            binding.top2.text = "Please grant permission"
            binding.top2.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.colorPrimary
                )
            )

            binding.top2.setOnClickListener {
                permissionIntentLauncher.launch(permission)
            }

        } else {
            getGsmInfo()
        }

    }

    private fun getGsmInfo() {

        listOfDetailsPairs.clear()

        val subscriptionManager =
            requireActivity().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager


        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

        val totalSubscriptionInfoCount = subscriptionManager.activeSubscriptionInfoCountMax

        binding.top1.text = "SIM Available"
        binding.top2.text = "Total: $totalSubscriptionInfoCount"

        var hasESim = false

        if (activeSubscriptionInfoList != null) {
            for (subscriptionInfo in activeSubscriptionInfoList) {
                val subscriptionId = subscriptionInfo.subscriptionId
                val carrierName = subscriptionInfo.carrierName
                val countryIso = subscriptionInfo.countryIso
                val displayName = subscriptionInfo.displayName
                val simSlotIndex = subscriptionInfo.simSlotIndex
                val subscriptionType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    subscriptionInfo.subscriptionType
                } else {
                    "Unknown"
                }

                listOfDetailsPairs.add(Pair("Operator SIM $subscriptionId", carrierName.toString()))
                listOfDetailsPairs.add(Pair("Country SIM $subscriptionId", countryIso.toString()))
                listOfDetailsPairs.add(
                    Pair(
                        "Display Name SIM $subscriptionId", displayName.toString()
                    )
                )
                listOfDetailsPairs.add(
                    Pair(
                        "Slot SIM $subscriptionId", (simSlotIndex + 1).toString()
                    )
                )
                listOfDetailsPairs.add(
                    Pair(
                        "Subscription Type SIM $subscriptionId",
                        if (subscriptionType == SubscriptionManager.SUBSCRIPTION_TYPE_LOCAL_SIM) {
                            "Local SIM"
                        } else {
                            "e-SIM"
                        }
                    )
                )

                if (subscriptionType == SubscriptionManager.SUBSCRIPTION_TYPE_REMOTE_SIM) {
                    hasESim = true
                }

            }
        }

        val eSimSupported = if (hasESim) {
            "Yes"
        } else {
            "No"
        }

        listOfDetailsPairs.add(Pair("e-SIM Supported", eSimSupported))

        adapter.updateList(listOfDetailsPairs)


    }


}