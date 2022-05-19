package com.example.myfinancialdash.api

import com.example.myfinancialdash.utils.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance_Crypto() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성


            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(CryptoConstants.BASE_URL_CRYPTO)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(CryptoAPI::class.java)
        }
    }
}

class RetrofitInstance_KorStockDetail() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(KorStockConstants.BASE_URL_KOR_STOCK)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(KorStockAPI::class.java)
        }
    }
}

class RetrofitInstance_KorStockChart() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(KorStockConstants.BASE_URL_KOR_CHART)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(KorStockAPI::class.java)
        }
    }
}

class RetrofitInstance_USDStock() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(UsdStockConstants.BASE_URL_USD_STOCK)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(UsdStockAPI::class.java)
        }
    }
}

class RetrofitInstance_IndexStock() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(IndexConstants.BASE_URL_INDEX)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(IndexAPI::class.java)
        }
    }
}

class RetrofitInstance_Dollar() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(IndexConstants.BASE_URL_DOLLAR)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(IndexAPI::class.java)
        }
    }
}

class RetrofitInstance_Bond() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(IndexConstants.BASE_URL_10YT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(IndexAPI::class.java)
        }
    }
}

class RetrofitInstance_StockSearch() {

    companion object{
        private val retrofit by lazy{
            //인터셉터 추가
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            //클라이언트 생성
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //api에서 Json으로 가져온 데이터를 gson으로 코틀린에서 읽기 가능하게 변환 하는작업
            Retrofit.Builder()
                .baseUrl(SearchConstants.BASE_URL_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }

        val api by lazy{
            retrofit.create(StockSearchAPI::class.java)
        }
    }
}

