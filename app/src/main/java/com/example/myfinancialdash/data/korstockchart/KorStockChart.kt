package com.example.myfinancialdash.data.korstockchart

data class KorStockChart(
    val code: String,
    val decimalUnit: Int,
    val hasVolume: Boolean,
    val infoType: String,
    val newlyListed: Boolean,
    val periodType: String,
    val priceInfos: List<PriceInfo>,
    val stockExchangeType: String
)