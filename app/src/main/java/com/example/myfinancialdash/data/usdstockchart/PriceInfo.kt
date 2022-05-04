package com.example.myfinancialdash.data.usdstockchart

data class PriceInfo(
    val accumulatedTradingVolume: Int,
    val closePrice: Double,
    val highPrice: Double,
    val localDate: String,
    val lowPrice: Double,
    val openPrice: Double
)