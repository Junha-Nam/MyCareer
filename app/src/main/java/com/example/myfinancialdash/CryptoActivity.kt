package com.example.myfinancialdash

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import com.example.myfinancialdash.api.RetrofitInstance
import com.example.myfinancialdash.databinding.ActivityCryptoBinding
import kotlinx.coroutines.*

class CryptoActivity : FragmentActivity() {

    val binding by lazy { ActivityCryptoBinding.inflate(layoutInflater) }

    private var job_search: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root )

        
        // 여기는 우측 대시보드. 즉, 실시간으로 계속 새로고침? 갱신 이 되어야 하는 곳이야. 
        
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofitTest = RetrofitInstance

                for(i in 1..1000) {

                    delay(1000)
                    println("test "+i.toString())


//                    val callTest = retrofitTest.api.getCryptoChart("KRW-BTC", 1)
//                    val test = callTest.body()
//                    println(test?.get(0)?.candle_date_time_kst.toString())

                }



            } catch(e:Exception) {
                e.printStackTrace()

            }

        }




        var check = "0"
        binding.searchCrpyto.setOnClickListener {
            job_search?.cancel()
            val korean_name = "비트코인"
            val retrofitCryptoSearch = RetrofitInstance
            var searchMarket = ""
            job_search = CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                try {

                    // 1. 한글명으로 MarketCode 찾기
                    val cryptoSearch = retrofitCryptoSearch.api.getCryptoSearch()
                    val cryptoSearchBody = cryptoSearch.body()


                    if (cryptoSearchBody != null) {
                        for (i in cryptoSearchBody) {
                            if (i.korean_name == korean_name && i.market.contains("KRW")) {
                                println(i.market)
                                searchMarket = i.market
                            } else {
                                // 여기에 정보가 없으면 메세지를 띄우고 해당 코루틴을 스톱을 시키면 되지아느까?
                            }
                        }
                    }

                    // 2. 차트정보 가져와 차트그리기
                    val cryptoChart = retrofitCryptoSearch.api.getCryptoChart(searchMarket,100)
                    val cryptoChartBody = cryptoChart.body()

                    // 2-1. 차트 그리기

                    // 3. 세부정보 가져와 표에 뿌리기
                    // 여기 부분이 무한루프가 돌아야됨. 재 검색 버튼을 누르거나, 화면이 넘어갈때 초기화.
                    val cryptoDetail = retrofitCryptoSearch.api.getCryptoDetail(searchMarket)
                    val cryptoDetailBody = cryptoDetail.body()

                    // 3-1. 표에 뿌리기
                    for(i in 1..1000) {

                        delay(1000)

                        println("testㄴㄷㅁㄱ "+i.toString())


//                    val callTest = retrofitTest.api.getCryptoChart("KRW-BTC", 1)
//                    val test = callTest.body()
//                    println(test?.get(0)?.candle_date_time_kst.toString())

                    }

                } catch(e:Exception) {
                    e.printStackTrace()

                }

            }

        }

        binding.buttonStock.setOnClickListener{
            job.cancel()
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)

        }
    }
}