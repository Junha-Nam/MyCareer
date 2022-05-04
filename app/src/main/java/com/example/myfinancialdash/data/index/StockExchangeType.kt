package com.example.myfinancialdash.data.index

data class StockExchangeType(
    val closePriceSendTime: String,
    val code: String,
    val delayTime: Int,
    val endTime: String,
    val name: String,
    val nameEng: String,
    val nameKor: String,
    val nationCode: String,
    val nationName: String,
    val nationType: String,
    val startTime: String,
    val zoneId: String
)