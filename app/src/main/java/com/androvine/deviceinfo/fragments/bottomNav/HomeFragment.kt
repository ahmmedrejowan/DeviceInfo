package com.androvine.deviceinfo.fragments.bottomNav

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androvine.deviceinfo.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val brand = Build.BRAND
        val model = Build.MODEL
        val device = Build.DEVICE
        val product = Build.PRODUCT
        val manufacturer = Build.MANUFACTURER
        val hardware = Build.HARDWARE
        val board = Build.BOARD
        val bootloader = Build.BOOTLOADER
        val display = Build.DISPLAY
        val fingerprint = Build.FINGERPRINT
        val host = Build.HOST
        val id = Build.ID
        val tags = Build.TAGS
        val type = Build.TYPE
        val user = Build.USER
        val codename = Build.VERSION.CODENAME
        val incremental = Build.VERSION.INCREMENTAL
        val release = Build.VERSION.RELEASE



    }


}