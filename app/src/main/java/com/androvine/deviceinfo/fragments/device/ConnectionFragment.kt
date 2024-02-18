package com.androvine.deviceinfo.fragments.device

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.databinding.FragmentConnectionBinding
import java.net.Inet4Address


class ConnectionFragment : Fragment() {

    private val binding by lazy { FragmentConnectionBinding.inflate(layoutInflater) }

    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

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


        registerNetworkCallback()

        setupConnection()
    }

    private fun registerNetworkCallback() {

        val request = NetworkRequest.Builder()
            .addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_CELLULAR)
            .build()

        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                setupConnection()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                setupNoConnection()
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
        val mobileNetwork = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(mobileNetwork)
        if (linkProperties != null) {
            val ipAddress = linkProperties.linkAddresses.find { it.address is Inet4Address }?.address?.hostAddress
            Log.e("ConnectionFragment", "IP Address: $ipAddress")
            linkProperties.dnsServers.forEach {
                Log.e("ConnectionFragment", "DNS Server: $it")
            }
            linkProperties.linkAddresses.forEach {
                Log.e("ConnectionFragment", "Link Address: $it")
            }
            linkProperties.domains?.forEach {
                Log.e("ConnectionFragment", "Domain: $it")
            }
            linkProperties.routes.forEach {
                Log.e("ConnectionFragment", "Route: $it")
            }
            linkProperties.interfaceName?.let {
                Log.e("ConnectionFragment", "Interface Name: $it")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                linkProperties.isPrivateDnsActive.let {
                    Log.e("ConnectionFragment", "Private DNS Active: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                linkProperties.dhcpServerAddress?.let {
                    Log.e("ConnectionFragment", "DHCP Server Address: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                linkProperties.mtu.let {
                    Log.e("ConnectionFragment", "MTU: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                linkProperties.nat64Prefix?.let {
                    Log.e("ConnectionFragment", "NAT64 Prefix: $it")
                }
            }
            linkProperties.httpProxy?.let {
                Log.e("ConnectionFragment", "HTTP Proxy: $it")
            }


        }


    }

    private fun setUpWifi() {
        val wifiNetwork = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(wifiNetwork)
        if (linkProperties != null) {
            val ipAddress = linkProperties.linkAddresses.find { it.address is Inet4Address }?.address?.hostAddress
            Log.e("ConnectionFragment", "IP Address: $ipAddress")
            linkProperties.dnsServers.forEach {
                Log.e("ConnectionFragment", "DNS Server: $it")
            }
            linkProperties.linkAddresses.forEach {
                Log.e("ConnectionFragment", "Link Address: $it")
            }
            linkProperties.domains?.forEach {
                Log.e("ConnectionFragment", "Domain: $it")
            }
            linkProperties.routes.forEach {
                Log.e("ConnectionFragment", "Route: $it")
            }
            linkProperties.interfaceName?.let {
                Log.e("ConnectionFragment", "Interface Name: $it")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                linkProperties.isPrivateDnsActive.let {
                    Log.e("ConnectionFragment", "Private DNS Active: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                linkProperties.dhcpServerAddress?.let {
                    Log.e("ConnectionFragment", "DHCP Server Address: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                linkProperties.mtu.let {
                    Log.e("ConnectionFragment", "MTU: $it")
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                linkProperties.nat64Prefix?.let {
                    Log.e("ConnectionFragment", "NAT64 Prefix: $it")
                }
            }
            linkProperties.httpProxy?.let {
                Log.e("ConnectionFragment", "HTTP Proxy: $it")
            }


        }

    }

    private fun setupNoConnection() {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetworkCallback()
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


}