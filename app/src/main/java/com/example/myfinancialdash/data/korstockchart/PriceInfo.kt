package com.example.myfinancialdash.data.korstockchart

data class PriceInfo(
    val accumulatedTradingVolume: Int,
    val closePrice: Double,
    val foreignRetentionRate: Double,
    val highPrice: Double,
    val localDate: String,
    val lowPrice: Double,
    val openPrice: Double
)