package com.example.myfinancialdash.data.usdstock

data class OverMarketPriceInfo(
    val compareToPreviousClosePrice: String,
    val compareToPreviousPrice: CompareToPreviousPriceX,
    val fluctuationsRatio: String,
    val localTradedAt: String,
    val overMarketStatus: String,
    val overPrice: String,
    val tradingSessionType: String
)