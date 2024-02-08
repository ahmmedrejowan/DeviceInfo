package com.androvine.icons

class AndroidVersionIcon {

    private val versionList = mutableListOf<AndroidVersionModel>()

    init {
        setupVersionList()

    }

    private fun setupVersionList() {

        versionList.add(
            AndroidVersionModel(
                "Android Gingerbread",
                "Gingerbread",
                "2.3",
                9,
                R.drawable.ic_android_gingerbread
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Gingerbread",
                "Gingerbread",
                "2.3.3",
                10,
                R.drawable.ic_android_gingerbread
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Honeycomb",
                "Honeycomb",
                "3.0",
                11,
                R.drawable.ic_android_honeycomb
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Honeycomb",
                "Honeycomb",
                "3.1",
                12,
                R.drawable.ic_android_honeycomb
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Honeycomb",
                "Honeycomb",
                "3.2",
                13,
                R.drawable.ic_android_honeycomb
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Ice Cream Sandwich",
                "Ice Cream Sandwich",
                "4.0",
                14,
                R.drawable.ic_android_ice_cream_sandwich
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Ice Cream Sandwich",
                "Ice Cream Sandwich",
                "4.0.3",
                15,
                R.drawable.ic_android_ice_cream_sandwich
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Jelly Bean",
                "Jelly Bean",
                "4.1",
                16,
                R.drawable.ic_android_jelly_bean
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Jelly Bean",
                "Jelly Bean",
                "4.2",
                17,
                R.drawable.ic_android_jelly_bean
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Jelly Bean",
                "Jelly Bean",
                "4.3",
                18,
                R.drawable.ic_android_jelly_bean
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android KitKat",
                "Key Lime Pie",
                "4.4",
                19,
                R.drawable.ic_android_kitkat
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android KitKat",
                "Key Lime Pie",
                "4.4W - 4.4W.2",
                20,
                R.drawable.ic_android_kitkat
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Lollipop",
                "Lemon Meringue Pie",
                "5.0 - 5.0.2",
                21,
                R.drawable.ic_android_lollipop
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Lollipop",
                "Lemon Meringue Pie",
                "5.1 - 5.1.1",
                22,
                R.drawable.ic_android_lollipop
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Marshmallow",
                "Macadamia Nut Cookie",
                "6.0 - 6.0.1",
                23,
                R.drawable.ic_android_marshmallow
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Nougat",
                "New York Cheesecake",
                "7.0",
                24,
                R.drawable.ic_android_nougat
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Nougat",
                "New York Cheesecake",
                "7.1 - 7.1.2",
                25,
                R.drawable.ic_android_nougat
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Oreo",
                "Oatmeal Cookie",
                "8.0",
                26,
                R.drawable.ic_android_oreo
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Oreo",
                "Oatmeal Cookie",
                "8.1",
                27,
                R.drawable.ic_android_oreo
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android Pie",
                "Pistachio Ice Cream",
                "9",
                28,
                R.drawable.ic_android_pie
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 10",
                "Quince Tart",
                "10",
                29,
                R.drawable.ic_android_10
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 11",
                "Red Velvet Cake",
                "11",
                30,
                R.drawable.ic_android_11
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 12",
                "Snow Cone",
                "12",
                31,
                R.drawable.ic_android_12
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 12L",
                "Snow Cone v2",
                "12.1",
                32,
                R.drawable.ic_android_12
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 13",
                "Tiramisu",
                "13",
                33,
                R.drawable.ic_android_13
            )
        )
        versionList.add(
            AndroidVersionModel(
                "Android 14",
                "Upside Down Cake",
                "14",
                34,
                R.drawable.ic_android_14
            )
        )


    }

    fun getVersionList(): List<AndroidVersionModel> {
        return versionList
    }

    fun getVersionByApiLevel(apiLevel: Int): AndroidVersionModel? {
        for (version in versionList) {
            if (version.apiLevel == apiLevel) {
                return version
            }
        }
        return null
    }
}


data class AndroidVersionModel(
    val name: String,
    val codeName: String,
    val version: String,
    val apiLevel: Int,
    val image: Int
)

