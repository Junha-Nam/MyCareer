package com.example.myfinancialdash.data.korstock

data class TotalInfo(
    val code: String,
    val compareToPreviousPrice: CompareToPreviousPrice,
    val key: String,
    val value: String,
    val valueDesc: String
)