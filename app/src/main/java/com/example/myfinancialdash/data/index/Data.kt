package com.example.myfinancialdash.data.index

data class Data(
    val accumulatedTradingValue: String,
    val accumulatedTradingVolume: String,
    val closePrice: String,
    val compareToPreviousClosePrice: String,
    val compareToPreviousPrice: CompareToPreviousPrice,
    val fluctuationsRatio: String,
    val highPrice: String,
    val itemCode: String,
    val localTradedAt: String,
    val lowPrice: String,
    val marketStatus: String,
    val openPrice: String,
    val stockExchangeType: StockExchangeType,
    val stockName: String,
    val symbolCode: String
)