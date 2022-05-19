package com.example.myfinancialdash.api

import com.example.myfinancialdash.data.index.Bond10YT
import com.example.myfinancialdash.data.index.Dollar
import com.example.myfinancialdash.data.index.IndexKorea
import com.example.myfinancialdash.data.index.IndexUsd
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface
IndexAPI {

    @GET("domestic/index/KOSPI%2CKOSDAQ") //우리는 headline news만 가져올것이기 때문에 엔드포인트를 v2/top-headlines로 설정해줍니다.
    suspend fun getKorIndex(
    ): Response<IndexKorea> //플러그인을 통해 만들어 주었던 데이터 클래스를 연결해줍니다.
                              //데이터 클래스 명을 써주세요

    //두번째 엔드포인트는 everything을 써주었습니다. 위 방식과 동일합니다.
    @GET("worldstock/index/.DJI%2C.IXIC%2C.DJT%2C.NDX%2C.INX%2C.SOX%2C.VIX")
    suspend fun getUsdIndex(
    ):Response<IndexUsd>

    @GET("majors")    suspend fun getIndexDollar(
    ):Response<Dollar>

    @GET("majors")
    suspend fun getIndexBond(
    ):Response<Bond10YT>


}