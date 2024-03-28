package com.rejowan.deviceinfo.fragments.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rejowan.deviceinfo.adapter.NetworkDetailsListAdapter
import com.rejowan.deviceinfo.databinding.FragmentNetworkBinding


class NetworkFragment : Fragment() {

    private val binding by lazy {
        FragmentNetworkBinding.inflate(layoutInflater)
    }

    private val listOfWifiPairs = mutableListOf<Pair<String, String>>()
    private val listOfBluetoothPairs = mutableListOf<Pair<String, String>>()
    private val listOfNFCPairs = mutableListOf<Pair<String, String>>()

    private val adapterForNFC: NetworkDetailsListAdapter by lazy {
        NetworkDetailsListAdapter(
            mutableListOf()
        )
    }

    private val adapterForBluetooth: NetworkDetailsListAdapter by lazy {
        NetworkDetailsListAdapter(
            mutableListOf()
        )
    }

    private val adapterForWifi: NetworkDetailsListAdapter by lazy {
        NetworkDetailsListAdapter(
            mutableListOf()
        )
    }

    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action == android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                getWifiInfo()
            }

            if (intent?.action == android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED) {
                getBluetoothInfo()
            }

            if (intent?.action == android.nfc.NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
                getNFCInfo()
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


        binding.nfcRecyclerView.adapter = adapterForNFC
        binding.nfcRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        binding.bluetoothRecyclerView.adapter = adapterForBluetooth
        binding.bluetoothRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        binding.wifiRecyclerView.adapter = adapterForWifi
        binding.wifiRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )


        getWifiInfo()

        getBluetoothInfo()

        getNFCInfo()


        requireActivity().registerReceiver(
            networkChangeReceiver,
            android.content.IntentFilter().apply {
                addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION)
                addAction(android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED)
                addAction(android.nfc.NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
            })

    }

    private fun getNFCInfo() {

        listOfNFCPairs.clear()

        val nfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(requireContext())

        if (nfcAdapter != null) {
            listOfNFCPairs.add(Pair("NFC", "Available"))


            nfcAdapter.isEnabled.let {
                listOfNFCPairs.add(Pair("NFC Enabled", if (it) "Yes" else "No"))
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                nfcAdapter.isSecureNfcSupported.let {
                    listOfNFCPairs.add(Pair("Secure NFC Supported", if (it) "Yes" else "No"))
                }
            } else {
                listOfNFCPairs.add(Pair("Secure NFC Supported", "N/A"))
            }


            adapterForNFC.updateList(listOfNFCPairs)

            binding.nfcTop1.text = "NFC Available"
            binding.nfcTop2.text = "This device supports NFC"


        } else {

            binding.nfcTop1.text = "Not Available"
            binding.nfcTop2.text = "This device does not support NFC"
            binding.parentNFC2.visibility = View.GONE

        }


    }

    private fun getBluetoothInfo() {


        listOfBluetoothPairs.clear()

        binding.bluetoothTop1.text = "Bluetooth Available"
        binding.bluetoothTop2.text = "This device supports Bluetooth"

        val bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.isMultipleAdvertisementSupported.let {
            listOfBluetoothPairs.add(Pair("Multiple Advertisement Supported",if (it) "Yes" else "No"))
        }

        bluetoothAdapter.isOffloadedFilteringSupported.let {
            listOfBluetoothPairs.add(Pair("Offloaded Filtering Supported", if (it) "Yes" else "No"))
        }

        bluetoothAdapter.isOffloadedScanBatchingSupported.let {
            listOfBluetoothPairs.add(Pair("Offloaded Scan Batching Supported", if (it) "Yes" else "No"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter.isLe2MPhySupported.let {
                listOfBluetoothPairs.add(Pair("LE 2M PHY Supported", if (it) "Yes" else "No"))
            }
        } else {
            listOfBluetoothPairs.add(Pair("LE 2M PHY Supported", "N/A"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter.isLeCodedPhySupported.let {
                listOfBluetoothPairs.add(Pair("LE Coded PHY Supported", if (it) "Yes" else "No"))
            }
        } else {
            listOfBluetoothPairs.add(Pair("LE Coded PHY Supported", "N/A"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter.isLeExtendedAdvertisingSupported.let {
                listOfBluetoothPairs.add(Pair("LE Extended Advertising Supported", if (it) "Yes" else "No"))
            }
        } else {
            listOfBluetoothPairs.add(Pair("LE Extended Advertising Supported", "N/A"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter.isLePeriodicAdvertisingSupported.let {
                listOfBluetoothPairs.add(Pair("LE Periodic Advertising Supported", if (it) "Yes" else "No"))
            }
        } else {
            listOfBluetoothPairs.add(Pair("LE Periodic Advertising Supported", "N/A"))
        }

        adapterForBluetooth.updateList(listOfBluetoothPairs)

    }

    private fun getWifiInfo() {

        listOfWifiPairs.clear()


        val wifiManager =
            requireActivity().applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
        val wifiInfo = wifiManager.connectionInfo


        val linkSpeed =
            wifiInfo.linkSpeed.toString() + " " + android.net.wifi.WifiInfo.LINK_SPEED_UNITS
        listOfWifiPairs.add(Pair("Link Speed", linkSpeed))

        val wifi5GhzSupported = if (wifiManager.is5GHzBandSupported) "Yes" else "No"
        listOfWifiPairs.add(Pair("5 GHz Supported", wifi5GhzSupported))

        val wifi6GhzSupported = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (wifiManager.is6GHzBandSupported) "Yes" else "No"
        } else {
            "N/A"
        }
        listOfWifiPairs.add(Pair("6 GHz Supported", wifi6GhzSupported))

        listOfWifiPairs.forEach {
            Log.d("WifiInfo", "${it.first} : ${it.second}")
        }


        binding.wifiTop1.text = "WiFi Available"
        binding.wifiTop2.text =
            wifiInfo.linkSpeed.toString() + " " + android.net.wifi.WifiInfo.LINK_SPEED_UNITS

        adapterForWifi.updateList(listOfWifiPairs)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(networkChangeReceiver)
    }


}