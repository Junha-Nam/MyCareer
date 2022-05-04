package com.example.myfinancialdash.data.usdstock

data class UsdStockDetail(
    val datas: List<Data>,
    val pollingInterval: Int,
    val time: String
)