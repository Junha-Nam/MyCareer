package com.example.myfinancialdash.data.stocksearch

data class Item(
    val code: String,
    val name: String,
    val nationCode: String,
    val nationName: String,
    val reutersCode: String,
    val typeCode: String,
    val typeName: String,
    val url: String
)