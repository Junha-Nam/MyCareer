package com.example.myfinancialdash.api

import com.example.myfinancialdash.data.stocksearch.StockSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface StockSearchAPI {

    @GET("ac")
    suspend fun getStockSearch(
        @Query("q") //parameter를 넣어줍니다.
        q:String = "애플", //기본값으로 US를 넣어주었습니다.
        @Query("target")
        target:String ="stock,index,marketindicator"
    ): Response<StockSearch> //플러그인을 통해 만들어 주었던 데이터 클래스를 연결해줍니다.
    //데이터 클래스 명을 써주세요

}