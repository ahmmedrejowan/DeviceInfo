package com.rejowan.icons

class BrandIcons() {

    private val brandIconList = mutableListOf<BrandIconModel>()

    init {
        setupBrandIconList()

    }

    private fun setupBrandIconList() {
        brandIconList.add(BrandIconModel("Kogan", R.drawable.ic_kogan))
        brandIconList.add(BrandIconModel("Symphony", R.drawable.ic_symphony))
        brandIconList.add(BrandIconModel("Walton", R.drawable.ic_walton))
        brandIconList.add(BrandIconModel("BBK", R.drawable.ic_bbk))
        brandIconList.add(BrandIconModel("Coolpad", R.drawable.ic_coolpad))
        brandIconList.add(BrandIconModel("Haier", R.drawable.ic_haier))
        brandIconList.add(BrandIconModel("Hisense", R.drawable.ic_hisense))
        brandIconList.add(BrandIconModel("Honor", R.drawable.ic_honor))
        brandIconList.add(BrandIconModel("Huawei", R.drawable.ic_huawei))
        brandIconList.add(BrandIconModel("Itel", R.drawable.ic_itel))
        brandIconList.add(BrandIconModel("iQOO", R.drawable.ic_iqoo))
        brandIconList.add(BrandIconModel("OnePlus", R.drawable.ic_oneplus))
        brandIconList.add(BrandIconModel("Oppo", R.drawable.ic_oppo))
        brandIconList.add(BrandIconModel("Realme", R.drawable.ic_realme))
        brandIconList.add(BrandIconModel("Vivo", R.drawable.ic_vivo))
        brandIconList.add(BrandIconModel("Xiaomi", R.drawable.ic_xiaomi))
        brandIconList.add(BrandIconModel("ZTE", R.drawable.ic_zte))
        brandIconList.add(BrandIconModel("Nokia", R.drawable.ic_nokia))
        brandIconList.add(BrandIconModel("HMD", R.drawable.ic_hmd))
        brandIconList.add(BrandIconModel("Alcatel", R.drawable.ic_alcatel))
        brandIconList.add(BrandIconModel("Lenovo", R.drawable.ic_lenovo))
        brandIconList.add(BrandIconModel("Infinix", R.drawable.ic_infinix))
        brandIconList.add(BrandIconModel("Lava", R.drawable.ic_lava))
        brandIconList.add(BrandIconModel("Micromax", R.drawable.ic_micromax))
        brandIconList.add(BrandIconModel("Fujitsu", R.drawable.ic_fujitsu))
        brandIconList.add(BrandIconModel("Casio", R.drawable.ic_casio))
        brandIconList.add(BrandIconModel("Hitachi", R.drawable.ic_hitachi))
        brandIconList.add(BrandIconModel("Sharp", R.drawable.ic_sharp))
        brandIconList.add(BrandIconModel("Sony", R.drawable.ic_sony))
        brandIconList.add(BrandIconModel("Toshiba", R.drawable.ic_toshiba))
        brandIconList.add(BrandIconModel("Samsung", R.drawable.ic_samsung))
        brandIconList.add(BrandIconModel("Doro", R.drawable.ic_doro))
        brandIconList.add(BrandIconModel("Acer", R.drawable.ic_acer))
        brandIconList.add(BrandIconModel("Asus", R.drawable.ic_asus))
        brandIconList.add(BrandIconModel("HTC", R.drawable.ic_htc))
        brandIconList.add(BrandIconModel("Nothing", R.drawable.ic_nothing))
        brandIconList.add(BrandIconModel("BBK", R.drawable.ic_bbk))
        brandIconList.add(BrandIconModel("Google", R.drawable.ic_google))
        brandIconList.add(BrandIconModel("HP", R.drawable.ic_hp))
        brandIconList.add(BrandIconModel("Microsoft", R.drawable.ic_microsoft))
        brandIconList.add(BrandIconModel("Motorola", R.drawable.ic_motorola))
    }

    fun getBrandIconList(): List<BrandIconModel> {
        return brandIconList
    }

    fun getBrandIconByName(name: String): Int? {
        for (brand in brandIconList) {
            if (brand.name.lowercase().trim() == name.lowercase().trim()) {
                return brand.icon
            }
        }
        return null
    }


}

data class BrandIconModel(
    val name: String, val icon: Int
)