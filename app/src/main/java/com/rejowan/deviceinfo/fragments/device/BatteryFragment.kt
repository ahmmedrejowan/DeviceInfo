package com.rejowan.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.databinding.FragmentBatteryBinding
import com.rejowan.deviceinfo.utils.JavaUtils.getBatteryCapacity
import java.text.DecimalFormat


class BatteryFragment : Fragment() {

    companion object{
        fun getAmperage(context: Context): String {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            var batteryCurrent =
                -batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW).toFloat()

            //    Log.e("TAG", "getAmperage: " + batteryCurrent);
            return if (batteryCurrent < 0) {
                // discharging state
                if (Math.abs(batteryCurrent / 1000) < 1.0) {
                    batteryCurrent = batteryCurrent * 1000
                }
                val df = DecimalFormat("#.##")
                df.format((batteryCurrent / 1000).toDouble())
            } else {
                // charging state
                if (Math.abs(batteryCurrent) > 100000.0) {
                    batteryCurrent = batteryCurrent / 1000
                }
                val df = DecimalFormat("#.##")
                df.format(batteryCurrent.toDouble())
            }
        }
    }

    private val binding by lazy { FragmentBatteryBinding.inflate(layoutInflater) }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var isChargingBoolean = false
    private var voltage: Double = 0.0
    var capacity = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }


    private val batteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {

            val deviceStatus = intent!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging =
                deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING || deviceStatus == BatteryManager.BATTERY_STATUS_FULL
            isChargingBoolean = isCharging

            if (isCharging) {
                when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                    BatteryManager.BATTERY_PLUGGED_AC -> {
                        binding.status.text = "Charging (AC)"
                    }
                    BatteryManager.BATTERY_PLUGGED_USB -> {
                        binding.status.text = "Charging (USB)"
                    }
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> {
                        binding.status.text = "Charging (Wireless)"
                    }
                    else -> {
                        binding.status.text = "Charging (Unknown)"
                    }
                }


            } else {
                binding.status.text = "Discharging"
                binding.chargingWattage.text = "Not Charging"
            }


            // battery voltage
            var d = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1).toDouble()
            java.lang.Double.isNaN(d)
            if (d > 12) {
                d /= 1000.0
            }
            val decimalFormat = DecimalFormat("#.##")
            binding.voltage.text = decimalFormat.format(d) + " V "


            // battery temperature
            val batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            binding.temperature.text = """
                            ${batteryTemperature / 10}°C
                            ${batteryTemperature / 10 * 9 / 5 + 32}°F
                            """.trimIndent()


            // battery health
            when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
                BatteryManager.BATTERY_HEALTH_COLD -> binding.batteryHealth.text = "Cold"

                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.batteryHealth.text = "Dead"
                }

                BatteryManager.BATTERY_HEALTH_GOOD -> binding.batteryHealth.text = "Good"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.batteryHealth.text = "Over Voltage"
                }

                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.batteryHealth.text = "Over Heat"
                }

                BatteryManager.BATTERY_HEALTH_UNKNOWN -> {
                    binding.batteryHealth.text = "Unknown"
                }

                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> {
                    binding.batteryHealth.text = "Unspecified Failure"
                }
            }


            // battery technology
            val batteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            binding.technology.text = batteryTechnology

            // battery level
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat()
            val df = DecimalFormat("#.##")
            binding.percentage.text = df.format(batteryPct * 100) + " %"


            // battery voltage
            var mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1).toDouble()
            java.lang.Double.isNaN(mVoltage)
            if (mVoltage > 12) {
                mVoltage /= 1000.0
            }
            voltage = mVoltage.toFloat().toDouble()


            // remaining battery mah
            val batteryTotalMah = getBatteryCapacity(context).toInt()
            val remainingMah = (batteryTotalMah * batteryPct).toInt()
            capacity = remainingMah.toFloat()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val batteryTotalMah: Int = getBatteryCapacity(context).toInt()

        binding.maxCapacity.text = "$batteryTotalMah mAh"
        binding.maxCapacity2.text = "$batteryTotalMah mAh"


        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            updateCharging()
            handler.postDelayed(runnable, 2000)
        }

        requireActivity().registerReceiver(
            batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

    }

    private fun updateCharging() {

        if (getAmperage(requireContext()).contains("-")) {
            binding.currentUsage.text = getAmperage(requireContext()) + " mAh (Discharging)"
            binding.currentUsage.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        } else {
            binding.currentUsage.text = getAmperage(requireContext()) + " mAh (Charging)"
            binding.currentUsage.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

       if (isChargingBoolean) {
           val amp = getAmperage(requireContext()).toFloat()
           val watt = voltage * amp / 1000
              binding.chargingWattage.text = DecimalFormat("#.##").format(watt) + " W"

       }

        binding.currentCapacity.text = DecimalFormat("#.##").format(capacity) + " mAh"


    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(batteryReceiver)
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }





}