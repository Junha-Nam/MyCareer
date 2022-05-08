
package com.example.myfinancialdash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.myfinancialdash.api.*
//import com.example.myfinancialdash.api.RetrofitInstance
import com.example.myfinancialdash.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : FragmentActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var jobSearch: Job? = null
    private var jobDashboard: Job? =null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root )


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 사실 구조는 되게 단순하다.
        // 1. 레트로핏을 써서 API에서 값을 가져온다. 갖고온 값을 뿌려준다. (우측화면)
        // 2. EditText에 값을 입력하고 검색을 했을 때, 세부내역 검색 내용이 돌면서 좌측에 값이 표시되고 몇 가지 값이 갱신된다.
        //    그럼 얘도 코루틴에서 값이 돌아야겠지?
        //
        // 그래서 Idea를 내면.. 반복문에는 우측 갱신 + Pointing 변수 값에 내용이 들어왔을 때 세부내역 중에서 갱신해야 되는 부분을 가져오는 ..
        // 1초에 한번 진행하면서 안멈추면 낮추자 초를..
        // 이 똑같은 구조를 Crypto에서 한번 더 반복하면 됨.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // 대시보드 만들기

        val retrofitDashboardIndex = RetrofitInstance_IndexStock
        val retrofitDashboardDollar = RetrofitInstance_Dollar
        val retrofitDashboardBond = RetrofitInstance_Bond
        jobDashboard = CoroutineScope(Dispatchers.IO).launch {
            try {
                while(true) {
                    // 값 가져오기
                    val korIndexSearch = retrofitDashboardIndex.api.getKorIndex()
                    val usdIndexSearch = retrofitDashboardIndex.api.getUsdIndex()
                    val dollarIndexSearch = retrofitDashboardDollar.api.getIndexDollar()
                    val bondIndexSearch = retrofitDashboardBond.api.getIndexBond()

                    // data에서 필요부분만 뽑아내기
                    val korIndexSearchBody = korIndexSearch.body()?.datas
                    val usdIndexSearchBody = usdIndexSearch.body()?.datas
                    val dollarIndexSearchBody = dollarIndexSearch.body()
                    val bondIndexSearchBody = bondIndexSearch.body()

                    // 코스피 코스닥
                    if (korIndexSearchBody != null) {
                        for (i in korIndexSearchBody) {
                            if (i.itemCode == "KOSPI") {
                                binding.kospiIndex.text = i.closePrice
                                binding.kospiRate.text = i.fluctuationsRatio
                            } else if (i.itemCode == "KOSDAQ") {
                                binding.kosdaqIndex.text = i.closePrice
                                binding.kosdaqRate.text = i.fluctuationsRatio
                            }
                        }
                    }

                    // snp500 나스닥 다우존스
                    if (usdIndexSearchBody != null) {
                        for (i in usdIndexSearchBody) {
                            if (i.reutersCode == ".DJI") {
                                binding.dowPrice.text = i.closePrice
                                binding.dowRate.text = i.fluctuationsRatio
                            } else if (i.reutersCode == ".IXIC") {
                                binding.NasdaqPrice.text = i.closePrice
                                binding.NasdaqRate.text = i.fluctuationsRatio
                            } else if (i.reutersCode == ".INX") {
                                binding.snp500Price.text = i.closePrice
                                binding.snp500Rate.text = i.fluctuationsRatio
                            }

                        }
                    }

                    // 달러

                    if (dollarIndexSearchBody != null) {
                        for (i in dollarIndexSearchBody) {
                            if (i.symbolCode == "USD") {
                                binding.dollarPrice.text = i.closePrice
                                binding.dollarRate.text = i.fluctuationsRatio
                                break
                            }
                        }
                    }

                    if (bondIndexSearchBody != null) {
                        for (i in bondIndexSearchBody) {
                            if (i.reutersCode == "US10YT=RR") {
                                binding.bondPrice.text = i.closePrice
                                binding.bondRate.text = i.fluctuationsRatio
                                break
                            }
                        }
                    }

                    delay(2000)
                }
                    //

                // 해당 박스에 다 뿌리기..
            } catch(e:Exception) {
                e.printStackTrace()

            }

        }

        // 우측 내용 실시간 표시
        // coroutine 을 사용

        // Idea 1. 세부 내역 검색을 누른순간 EditText에 있는 값을 변수 하나에 놔두고
        // 이 변수가 ""이 아니면 이 부분도 코루틴에서 반복을 수행한다.
        // 어디까지 가져올거냐.. 가 문젠데

        //



        // 검색버튼 클릭 시 동작.
        binding.searchStock.setOnClickListener{
            jobSearch?.cancel()
            // 1. 검색을 한다. 한국 미국 양쪽에서
            val search_name = binding.editStock.text.toString()
            var korean_name = ""
            if(search_name == "a") {
                korean_name = "애플"
            } else if (search_name == "b") {
                korean_name = "삼성전자"
            }
            // 2. 검색결과가 없거나 두개 이상이면 올바르게 검색해달라고 얘기하고 종료
            //     else이면 이제 동작.
            println(korean_name)
            val retrofitStockSearch = RetrofitInstance_StockSearch
            var searchMarket = ""
            var reutersCode = ""
            var nation = ""
            jobSearch = CoroutineScope(Dispatchers.IO).launch {
                try {
                    delay(500)
                    // 1. 한글명으로 MarketCode 찾기a
                    val stockSearch = retrofitStockSearch.api.getStockSearch(korean_name)
                    val stockSearchBody = stockSearch.body()?.items
                    println(stockSearchBody)
                    var isTrue = 0
                    if (stockSearchBody != null) {
                        for (i in stockSearchBody) {
                            if (i.name == korean_name) {
                                println(i.name)
                                searchMarket = i.reutersCode
                                korean_name = i.name
                                nation = i.nationName
                                isTrue = 1
                                break
                            } else {
                                // 여기에 정보가 없으면 메세지를 띄우고 해당 코루틴을 스톱을 시키면 되지아느까?
                            }
                        }

                    }
                    if (isTrue == 0) {
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(java.lang.Runnable {
                            Toast.makeText(applicationContext,"검색결과가 없습니다. 이름을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }, 0)

                        //Toast.makeText("") 여기다가 에러메세지 만들고 끝
                    } else {
                        // 세부정보 가져오기
                        // 나라명으로 해서 한국이면 한투api 아니면 네이버 api
                        if(nation =="대한민국") {

                        } else {
                        }
                        // 차트정보 가져와서 그리기

                        // 표 정보 가져와서 뿌리기

                    }





                } catch (e:Exception) {
                    e.printStackTrace()
                }



            }
            // 3. 이게 한국주식이면 한투 API를 탈거고 아니면 네이버 API를 탄다.

            // 4. 각각의 API에서 데이터들(차트, 세부정보) 가져와서 뿌린다.

            // 5. 저 4번을 반복한다. 구조는 CRYPTO랑 똑같이이

        }

        // crypto로 화면전환
        binding.buttonCrypto.setOnClickListener{
            jobDashboard?.cancel()
            jobSearch?.cancel()
            val nextIntent = Intent(this, CryptoActivity::class.java)
            startActivity(nextIntent)
            println("넘어가도 동작해?")
            finish()
            println("stop이후도 동작해?")



        }

        // 권한 승인에 관한 내용..

    }
}