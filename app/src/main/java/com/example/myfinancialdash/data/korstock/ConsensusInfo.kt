package com.example.myfinancialdash.data.korstock

data class ConsensusInfo(
    val createDate: String,
    val itemCode: String,
    val priceTargetMean: String,
    val recommMean: String
)