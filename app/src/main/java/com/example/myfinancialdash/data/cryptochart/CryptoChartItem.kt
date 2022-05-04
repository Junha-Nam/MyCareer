package com.example.myfinancialdash.data.cryptochart

data class CryptoChartItem(
    val candle_acc_trade_price: Double,
    val candle_acc_trade_volume: Double,
    val candle_date_time_kst: String,
    val candle_date_time_utc: String,
    val change_price: Double,
    val change_rate: Double,
    val high_price: Double,
    val low_price: Double,
    val market: String,
    val opening_price: Double,
    val prev_closing_price: Double,
    val timestamp: Long,
    val trade_price: Double
)