package com.example.myfinancialdash.data.korstock

data class KorStockClose(
    val closePrice: String,
    val compareToPreviousClosePrice: String,
    val compareToPreviousPrice: CompareToPreviousPriceX,
    val fluctuationsRatio: String,
    val itemCode: String,
    val reutersCode: String,
    val sosok: String,
    val stockEndType: String,
    val stockName: String
)