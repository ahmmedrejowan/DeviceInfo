package com.androvine.deviceinfo.fragments.device

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.NetworkDetailsListAdapter
import com.androvine.deviceinfo.databinding.BottomSheetPublicIpBinding
import com.androvine.deviceinfo.databinding.FragmentConnectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.URL


class ConnectionFragment : Fragment() {

    private val binding by lazy { FragmentConnectionBinding.inflate(layoutInflater) }

    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager
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

        connectivityManager =
            requireContext().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager.activeNetwork != null) {
            setupConnection()
        } else {
            setupNoConnection()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        registerNetworkCallback()


    }

    private fun registerNetworkCallback() {

        val request = NetworkRequest.Builder().addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR).build()

        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                requireActivity().runOnUiThread {
                    setupConnection()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                requireActivity().runOnUiThread {
                    setupNoConnection()
                }
            }
        }


        connectivityManager.registerNetworkCallback(request, networkCallback)


    }

    private fun setupConnection() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(TRANSPORT_WIFI)) {
                // Connected via WiFi
                setUpWifi()
            } else if (networkCapabilities.hasTransport(TRANSPORT_CELLULAR)) {
                // Connected via mobile data
                setUpMobileData()
            }
        }

    }

    private fun setUpMobileData() {

        listOfDetailsPairs.clear()
        getNetworkData()

        binding.networkImage.setImageResource(R.drawable.ic_mobile_network)
        binding.top1.text = "Mobile Data"

        binding.parentLayout2.visibility = View.VISIBLE

        adapter.updateList(listOfDetailsPairs)

        binding.publicIp.setOnClickListener {
            checkPublicIp()
        }

    }

    private fun checkPublicIp() {
        val urlString = "http://ip-api.com/json"
        var json: JSONObject? = null

        Toast.makeText(requireContext(), "Fetching Public IP...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {

            withContext(Dispatchers.IO) {
                try {
                    val url = URL(urlString)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connect()

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        val jsonString = inputStream.bufferedReader().use { it.readText() }
                        json = JSONObject(jsonString)
                    }
                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            withContext(Dispatchers.Main) {
                formatDetails(json)
            }

        }


    }

    private fun formatDetails(json: JSONObject?) {

        if (json != null) {
            val ip = json.getString("query")
            val country = json.getString("country")
            val timezone = json.getString("timezone")
            val isp = json.getString("isp")
            val asName = json.getString("as")

            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val bottomSheetBinding: BottomSheetPublicIpBinding = BottomSheetPublicIpBinding.inflate(
                layoutInflater
            )
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            bottomSheetDialog.window!!.setLayout(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
                )


            bottomSheetBinding.ipAddress.text = ip
            bottomSheetBinding.country.text = country
            bottomSheetBinding.timezone.text = timezone
            bottomSheetBinding.isp.text = "$isp ($asName)"


            bottomSheetBinding.btnDismiss.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()


        }
    }


    private fun setUpWifi() {

        listOfDetailsPairs.clear()
        getNetworkData()

        binding.networkImage.setImageResource(R.drawable.ic_wifi_network)
        binding.top1.text = "Wi-Fi"

        binding.parentLayout2.visibility = View.VISIBLE

        adapter.updateList(listOfDetailsPairs)

        binding.publicIp.setOnClickListener {
            checkPublicIp()
        }

    }


    private fun getNetworkData() {
        val network = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(network)
        if (linkProperties != null) {

            val ipAddress =
                linkProperties.linkAddresses.find { it.address is Inet4Address }?.address?.hostAddress
            linkProperties.dnsServers.forEach {}
            linkProperties.linkAddresses.forEach {}
            linkProperties.routes.forEach {}
            linkProperties.interfaceName?.let {}

            listOfDetailsPairs.add(Pair("IP Address", ipAddress.toString()))
            linkProperties.linkAddresses.forEach {
                val linkAddress = it.address
                if (linkAddress is Inet4Address) {
                    listOfDetailsPairs.add(
                        Pair(
                            "IPv4 Address", linkAddress.hostAddress?.toString() ?: "No IPv4 Address"
                        )
                    )
                } else if (linkAddress is Inet6Address) {
                    listOfDetailsPairs.add(
                        Pair(
                            "IPv6 Address", linkAddress.hostAddress?.toString() ?: "No IPv6 Address"
                        )
                    )
                }
            }
            linkProperties.dnsServers.forEachIndexed { index, inetAddress ->
                listOfDetailsPairs.add(
                    Pair(
                        "DNS Server ${index + 1}",
                        inetAddress.hostAddress?.toString() ?: "No DNS Server"
                    )
                )
            }


            listOfDetailsPairs.add(Pair("Interface Name", linkProperties.interfaceName.toString()))
            linkProperties.routes.forEach {
                val route = it
                if (route.isDefaultRoute) {
                    listOfDetailsPairs.add(
                        Pair(
                            "Gateway", route.gateway?.hostAddress?.toString() ?: "No Gateway"
                        )
                    )
                }
            }



            binding.top2.text = ipAddress.toString()


        }


    }


    private fun setupNoConnection() {
        binding.networkImage.setImageResource(R.drawable.ic_not_available)
        binding.top1.text = "No Connection"
        binding.top2.text = "Check your connection and try again"

        binding.parentLayout2.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetworkCallback()
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onResume() {
        super.onResume()
        if (connectivityManager.activeNetwork != null) {
            setupConnection()
        } else {
            setupNoConnection()
        }
    }

}