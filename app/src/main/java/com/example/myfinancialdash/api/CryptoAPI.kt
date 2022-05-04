package com.example.myfinancialdash.api

import com.example.myfinancialdash.data.korstock.KorStock
import com.example.myfinancialdash.utils.KorStockConstants.Companion.API_KEY_KOR_STOCK
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoAPI {

    @GET("v2/top-headlines") //우리는 headline news만 가져올것이기 때문에 엔드포인트를 v2/top-headlines로 설정해줍니다.
    suspend fun getBreakingNews(
        //Suspend는 안에있는 함수들이 실행될때까지 다른 스레드를 멈추게 합니다.
        //다 불러오지 못했는데 다른 작업이 데이터를 요청하면 에러를 불러올수있기때문에 suspend를 사용하면 안전하게 데이터를 가져올수있습니다.
        @Query("country") //parameter를 넣어줍니다.
        countryCode:String = "us", //기본값으로 US를 넣어주었습니다.
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY_KOR_STOCK //편의를 위해 API_KEY를 constant 클래스에 모아서 관리하겠습니다.
    ): Response<KorStock> //플러그인을 통해 만들어 주었던 데이터 클래스를 연결해줍니다.
                              //데이터 클래스 명을 써주세요

    //두번째 엔드포인트는 everything을 써주었습니다. 위 방식과 동일합니다.
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY_KOR_STOCK
    ):Response<KorStock>
}