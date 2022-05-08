package com.example.myfinancialdash.api

import com.example.myfinancialdash.data.usdstock.UsdStockDetail
import com.example.myfinancialdash.data.usdstockchart.UsdStockChart
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsdStockAPI {

    @GET("stock/{stok}/basic")
    suspend fun getUsdStock(
        @Path("stok") stok: String
    ): Response<UsdStockDetail>

    @GET("chart/foreign/item/{stock}")
    suspend fun getUsdChart(
        @Path("stock") stock: String,
        @Query("periodType")
        periodType:String = "month",
        @Query("range")
        range:String = "3"
    ): Response<UsdStockChart>
}