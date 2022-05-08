package com.example.myfinancialdash.data.usdstock

data class StockItemTotalInfo(
    val code: String,
    val compareToPreviousPrice: CompareToPreviousPriceX,
    val date: String,
    val key: String,
    val keyDesc: String,
    val value: String,
    val valueDesc: String
)