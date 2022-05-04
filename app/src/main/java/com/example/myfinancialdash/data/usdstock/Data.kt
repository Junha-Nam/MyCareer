package com.example.myfinancialdash.data.usdstock

data class Data(
    val accumulatedTradingValue: String,
    val accumulatedTradingVolume: String,
    val closePrice: String,
    val compareToPreviousClosePrice: String,
    val compareToPreviousPrice: CompareToPreviousPrice,
    val currencyType: CurrencyType,
    val fluctuationsRatio: String,
    val highPrice: String,
    val isinCode: String,
    val localTradedAt: String,
    val lowPrice: String,
    val marketStatus: String,
    val marketValueFull: String,
    val marketValueHangeul: String,
    val marketValueKrwHangeul: String,
    val myDataCode: Any,
    val openPrice: String,
    val overMarketPriceInfo: OverMarketPriceInfo,
    val reutersCode: String,
    val stockEndUrl: Any,
    val stockExchangeType: StockExchangeType,
    val stockName: String,
    val symbolCode: String,
    val tradeStopType: TradeStopType
)