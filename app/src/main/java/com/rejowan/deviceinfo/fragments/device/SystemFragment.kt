package com.rejowan.deviceinfo.fragments.device

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rejowan.deviceinfo.R
import com.rejowan.deviceinfo.databinding.FragmentSystemBinding
import com.rejowan.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import com.rejowan.icons.BrandIcons
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class SystemFragment : Fragment() {


    private val binding by lazy { FragmentSystemBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        deviceDetailsViewModel.systemDataModel.observe(viewLifecycleOwner){
            if (it!=null){

                val iconModel = BrandIcons().getBrandIconByName(it.brand!!)
                if (iconModel != null) {
                    binding.brandIcon.setImageResource(iconModel)
                } else {
                    binding.brandIcon.setImageResource(R.drawable.ic_android_partner)
                }
                binding.name.text = it.name
                binding.model.text = "(${it.model})"
                binding.brandName.text = it.brand
                binding.name2.text = it.name
                binding.model2.text = it.model
                binding.device.text = it.device
                binding.product.text = it.product
                binding.manufacturer.text = it.manufacturer
                binding.hardware.text = it.hardware
                binding.board.text = it.board
                binding.radio.text = it.radio




            }
        }



        binding.parentLayout.setOnTouchListener { _, event ->
            // Check if the touch event is outside the TextViews
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Clear text selection when the user touches outside
                clearTextSelection(view)
            }
            false
        }


    }

    private fun clearTextSelection(view: View) {
        // Iterate through all TextViews and clear text selection
        clearTextSelectionInView(view)
    }

    private fun clearTextSelectionInView(view: View) {
        if (view is TextView) {
            view.clearFocus()
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                clearTextSelectionInView(view.getChildAt(i))
            }
        }
    }

}