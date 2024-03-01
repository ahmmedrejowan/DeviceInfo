package com.androvine.deviceinfo.fragments.apps

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androvine.deviceinfo.R
import com.androvine.deviceinfo.adapter.AppsListAdapter
import com.androvine.deviceinfo.databinding.FragmentUserAppsBinding
import com.androvine.deviceinfo.detailsMVVM.DeviceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.util.concurrent.Executors


class UserAppsFragment : Fragment() {

    private val binding by lazy { FragmentUserAppsBinding.inflate(layoutInflater) }
    private val deviceDetailsViewModel: DeviceDetailsViewModel by activityViewModel()

    val userApps = mutableListOf<PackageInfo>()
    private var viewSelectedSort = "App Name (A-Z)"
    private val adapter: AppsListAdapter by lazy {
        AppsListAdapter(
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


        binding.sort.setOnClickListener {
            showSortPopupWindow()
        }


        deviceDetailsViewModel.userApps.observe(viewLifecycleOwner) {
            userApps.clear()
            userApps.addAll(it)
            binding.totalApps.text = "Total Apps: ${userApps.size}"
            changeSortUpdateAdapter(sort = viewSelectedSort)
        }

    }


    private fun showSortPopupWindow() {
        val linearLayout = LinearLayout(requireContext())
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.backgroundColor
            )
        )

        // padding
        val padding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        linearLayout.setPadding(padding, padding, padding, 0)

        val popupWindow = PopupWindow(
            linearLayout,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val sortList = listOf(
            "App Name (A-Z)",
            "App Name (Z-A)",
            "App Size (DESC)",
            "App Size (ASC)",
            "Install Date (DESC)",
            "Install Date (ASC)"
        )

        for (sort in sortList) {
            val textView = TextView(requireContext())
            textView.text = sort
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            textView.textSize = 14f

            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))

            when (sort) {
                sortList.first() -> {
                    textView.setPadding(20, 15, 20, 15)
                }

                sortList.last() -> {
                    textView.setPadding(20, 15, 20, 30)
                }

                else -> {
                    textView.setPadding(20, 15, 20, 15)
                }
            }

            textView.setOnClickListener {
                binding.sort.text = sort
                viewSelectedSort = sort

                changeSortUpdateAdapter(sort)

                popupWindow.dismiss()
            }

            linearLayout.addView(textView)


            // Adding a divider line
            val divider = View(requireContext())
            divider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1
            ) // height is 1 pixel

            divider.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.backgroundColorSecondary
                )
            ) // Replace with your desired color

            if (sort != sortList.last()) {
                linearLayout.addView(divider)

            }

        }

        popupWindow.showAsDropDown(binding.sort)

    }

    private fun changeSortUpdateAdapter(sort: String) {

        binding.progressIndicator.visibility = View.VISIBLE

        Executors.newSingleThreadExecutor().execute {
            when (sort) {
                "App Name (A-Z)" -> {
                    userApps.sortBy {
                        it.applicationInfo.loadLabel(requireActivity().packageManager).toString().lowercase()
                    }
                }

                "App Name (Z-A)" -> {
                    userApps.sortByDescending {
                        it.applicationInfo.loadLabel(requireActivity().packageManager).toString().lowercase()
                    }
                }

                "App Size (DESC)" -> {
                    userApps.sortByDescending {
                        File(it.applicationInfo.publicSourceDir).length()
                    }
                }

                "App Size (ASC)" -> {
                    userApps.sortBy {
                        File(it.applicationInfo.publicSourceDir).length()
                    }
                }

                "Install Date (DESC)" -> {
                    userApps.sortByDescending { it.firstInstallTime }
                }

                "Install Date (ASC)" -> {
                    userApps.sortBy { it.firstInstallTime }
                }
            }


            requireActivity().runOnUiThread {
                binding.progressIndicator.visibility = View.GONE
                adapter.updateList(userApps)
            }

        }


    }

}