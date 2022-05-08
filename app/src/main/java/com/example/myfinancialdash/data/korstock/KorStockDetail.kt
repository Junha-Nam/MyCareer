package com.example.myfinancialdash.data.korstock

data class KorStockDetail(
    val consensusInfo: ConsensusInfo,
    val description: Any,
    val industryCode: String,
    val irScheduleInfo: Any,
    val itemCode: String,
    val reutersCode: String,
    val shareholdersMeetingInfo: Any,
    val stockEndType: String,
    val stockName: String,
    val totalInfos: List<TotalInfo>
)