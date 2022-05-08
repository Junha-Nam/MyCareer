package com.example.myfinancialdash.api

import com.example.myfinancialdash.MainActivity
import com.example.myfinancialdash.data.korstock.KorStockClose
import com.example.myfinancialdash.data.korstock.KorStockDetail
import com.example.myfinancialdash.data.korstockchart.KorStockChart
import com.example.myfinancialdash.data.usdstock.UsdStockDetail
import com.example.myfinancialdash.data.usdstockchart.UsdStockChart
import com.example.myfinancialdash.utils.KorStockConstants
import retrofit2.Response
import retrofit2.http.*

interface KorStockAPI {

    @GET("api/stock/{stok}/integration")
    suspend fun getKorStock(
        @Path("stok") stok: String
    ): Response<KorStockDetail>

    @GET("api/stock/{stok}/basic")
    suspend fun getKorStockClose(
        @Path("stok") stok: String
    ): Response<KorStockClose>

    @GET("chart/domestic/item/{stock}")
    suspend fun getKorChart(
        @Path("stock") stock: String,
        @Query("periodType")
        periodType:String = "month",
        @Query("range")
        range:String = "3"
    ): Response<KorStockChart>


}