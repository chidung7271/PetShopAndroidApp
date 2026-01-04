package app.code.petshopandroidapp.ui.components.config.barchart

enum class StatisticMode {
        DAILY, MONTHLY, YEARLY;

    fun getVietnameseName(): String {
        return when (this) {
            DAILY -> "Ngày"
            MONTHLY -> "Tháng"
            YEARLY -> "Năm"
        }
    }
}