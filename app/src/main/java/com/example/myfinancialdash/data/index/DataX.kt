package com.example.myfinancialdash.data.index

data class DataX(
    val accumulatedTradingValue: String,
    val accumulatedTradingVolume: String,
    val closePrice: String,
    val compareToPreviousClosePrice: String,
    val compareToPreviousPrice: CompareToPreviousPrice,
    val fluctuationsRatio: String,
    val highPrice: String,
    val indexName: String,
    val localTradedAt: String,
    val lowPrice: String,
    val marketStatus: String,
    val openPrice: String,
    val reutersCode: String,
    val stockExchangeType: StockExchangeType,
    val symbolCode: String
)