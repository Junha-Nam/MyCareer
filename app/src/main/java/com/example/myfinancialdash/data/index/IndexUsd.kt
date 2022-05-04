package com.example.myfinancialdash.data.index

data class IndexUsd(
    val datas: List<DataX>,
    val pollingInterval: Int,
    val time: String
)