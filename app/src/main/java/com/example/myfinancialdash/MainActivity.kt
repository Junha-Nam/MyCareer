
package com.example.myfinancialdash

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.myfinancialdash.api.*
import com.example.myfinancialdash.data.korstockchart.KorStockChart
import com.example.myfinancialdash.data.usdstockchart.UsdStockChart
import com.example.myfinancialdash.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.kt.gigagenie.geniesdk.GenieSdkEventListener
import com.kt.gigagenie.geniesdk.data.model.Response
import com.kt.gigagenie.geniesdk.service.VoiceService
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : FragmentActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var jobSearch: Job? = null
    private var jobDashboard: Job? =null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(binding.root )


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 사실 구조는 되게 단순하다.


        // 1. 레트로핏을 써서 API 값을 가져온다. 갖고온 값을 뿌려준다. (우측화면)
        // 2. EditText 값을 입력하고 검색을 했을 때, 세부내역 검색 내용이 돌면서 좌측에 값이 표시되고 몇 가지 값이 갱신된다.
        //    그럼 얘도 코루틴에서 값이 돌아야겠지?
        //
        // 그래서 Idea 내면.. 반복문에는 우측 갱신 + Pointing 변수 값에 내용이 들어왔을 때 세부내역 중에서 갱신해야 되는 부분을 가져오는 ..
        // 1초에 한번 진행하면서 안멈추면 낮추자 초를..
        // 이 똑같은 구조를 Crypto 한번 더 반복하면 됨.
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // 대시보드 만들기
        val retrofitInstanceIndexStock = RetrofitInstance_IndexStock
        val retrofitInstanceDollar = RetrofitInstance_Dollar
        val retrofitInstanceBond = RetrofitInstance_Bond

        jobDashboard = CoroutineScope(Dispatchers.IO).launch {
            try {
                while(true) {
                    // 값 가져오기
                    val korIndexSearch = retrofitInstanceIndexStock.api.getKorIndex()
                    val usdIndexSearch = retrofitInstanceIndexStock.api.getUsdIndex()
                    val dollarIndexSearch = retrofitInstanceDollar.api.getIndexDollar()
                    val bondIndexSearch = retrofitInstanceBond.api.getIndexBond()

                    // data 필요부분만 뽑아내기
                    val korIndexSearchBody = korIndexSearch.body()?.datas
                    val usdIndexSearchBody = usdIndexSearch.body()?.datas
                    val dollarIndexSearchBody = dollarIndexSearch.body()
                    val bondIndexSearchBody = bondIndexSearch.body()

                    // 코스피 코스닥
                    if (korIndexSearchBody != null) {
                        for (i in korIndexSearchBody) {
//                            if (i.itemCode == "KOSPI") {
//                                runOnUiThread {
//                                    binding.kospiIndex.text = i.closePrice
//                                    binding.kospiRate.text = i.fluctuationsRatio
//                                }
//                            } else if (i.itemCode == "KOSDAQ") {
//                                runOnUiThread {
//                                    binding.kosdaqIndex.text = i.closePrice
//                                    binding.kosdaqRate.text = i.fluctuationsRatio
//                                }
//                            }
                            when(i.itemCode) {
                                "KOSPI" -> {
                                    runOnUiThread {
                                        binding.kospiIndex.text = i.closePrice
                                        binding.kospiRate.text = i.fluctuationsRatio + "%"
                                    }
                                }
                                "KOSDAQ" -> {
                                    runOnUiThread {
                                        binding.kosdaqIndex.text = i.closePrice
                                        binding.kosdaqRate.text = i.fluctuationsRatio + "%"
                                    }

                                }
                            }
                        }
                    }

                    // snp500 나스닥 다우존스
                    if (usdIndexSearchBody != null) {
                        for (i in usdIndexSearchBody) {
//                            if (i.reutersCode == ".DJI") {
//                                runOnUiThread {
//                                    binding.dowPrice.text = i.closePrice
//                                    binding.dowRate.text = i.fluctuationsRatio
//                                }
//                            } else if (i.reutersCode == ".IXIC") {
//                                runOnUiThread {
//                                    binding.NasdaqPrice.text = i.closePrice
//                                    binding.NasdaqRate.text = i.fluctuationsRatio
//                                }
//                            } else if (i.reutersCode == ".INX") {
//                                runOnUiThread {
//                                    binding.snp500Price.text = i.closePrice
//                                    binding.snp500Rate.text = i.fluctuationsRatio
//                                }
//                            }
                            when(i.reutersCode) {
                                ".DJI" -> {
                                    runOnUiThread {
                                        binding.dowPrice.text = i.closePrice
                                        binding.dowRate.text = i.fluctuationsRatio + "%"
                                    }
                                }
                                ".IXIC" -> {
                                    runOnUiThread {
                                        binding.NasdaqPrice.text = i.closePrice
                                        binding.NasdaqRate.text = i.fluctuationsRatio + "%"
                                    }
                                }
                                ".INX" -> {
                                    runOnUiThread {
                                        binding.snp500Price.text = i.closePrice
                                        binding.snp500Rate.text = i.fluctuationsRatio + "%"
                                    }
                                }
                            }

                        }
                    }

                    // 달러

                    if (dollarIndexSearchBody != null) {
                        for (i in dollarIndexSearchBody) {
                            if (i.symbolCode == "USD") {
                                runOnUiThread {
                                    binding.dollarPrice.text = i.closePrice
                                    binding.dollarRate.text = i.fluctuationsRatio + "%"
                                }
                                break
                            }
                        }
                    }

                    if (bondIndexSearchBody != null) {
                        for (i in bondIndexSearchBody) {
                            if (i.reutersCode == "US10YT=RR") {
                                runOnUiThread {
                                    binding.bondPrice.text = i.closePrice
                                    binding.bondRate.text = i.fluctuationsRatio + "%"
                                }
                                break
                            }
                        }
                    }

                    delay(1000)
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
        
        val retrofitInstanceStockSearch = RetrofitInstance_StockSearch
        val retrofitInstanceKorStockChart = RetrofitInstance_KorStockChart
        val retrofitInstanceKorStockDetail = RetrofitInstance_KorStockDetail
        val retrofitInstanceUsdStock = RetrofitInstance_USDStock


        binding.searchStock.setOnClickListener{
            jobSearch?.cancel()
            // 1. 검색을 한다. 한국 미국 양쪽에서
            val searchName = binding.editStock.text.toString().uppercase()
            var koreanName = searchName

            // 2. 검색결과가 없거나 두개 이상이면 올바르게 검색해달라고 얘기하고 종료
            //     else이면 이제 동작.
            var reutersCode = ""
            var nation = ""
            jobSearch = CoroutineScope(Dispatchers.IO).launch {
                try {
                    delay(500)
                    // 1. 한글명으로 MarketCode 찾기
                    val stockSearch = retrofitInstanceStockSearch.api.getStockSearch(koreanName)
                    val stockSearchBody = stockSearch.body()?.items
                    var isTrue = 0
                    if (stockSearchBody != null) {
                        for (i in stockSearchBody) {
                            if (i.name == koreanName || i.name.contains("$koreanName Class")) {
                                reutersCode = i.reutersCode
                                koreanName = i.name
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
                        //

                        if (nation == "대한민국") {
                            //한국 차트
                            val korStockChart = retrofitInstanceKorStockChart.api.getKorChart(reutersCode)
                            val korStockChartBody = korStockChart.body()
                            initChart()
                            if (korStockChartBody != null) {
                                setChartDataKor(korStockChartBody)
                            }
                            // 한국 상세
                            runOnUiThread {
                                binding.stockName.text = koreanName
                            }
                            var count = 0
                            // 3-1. 표에 뿌리기
                            while (true) {
                                val korStockDetail = retrofitInstanceKorStockDetail.api.getKorStock(reutersCode)
                                val korStockClose = retrofitInstanceKorStockDetail.api.getKorStockClose(reutersCode)
                                println("여긴?")
                                val korStockDetailBody = korStockDetail.body()
                                val korStockDetailClose = korStockClose.body()
                                println("여긴 오냐?")
                                val korStockDetailInfos = korStockDetailBody?.totalInfos
                                println(korStockDetailBody)

                                runOnUiThread {

                                    // UI변경 부분을 입력하자
                                    binding.stockPrice.text =
                                        korStockDetailClose?.closePrice.toString()
                                    binding.stockPercent.text =
                                        korStockDetailClose?.fluctuationsRatio.toString() + "%"
                                    binding.openingPrice.text =
                                        korStockDetailInfos?.get(1)?.value.toString()
                                    binding.dividend.text =
                                        korStockDetailInfos?.get(16)?.value.toString()
                                    binding.highPrice.text =
                                        korStockDetailInfos?.get(2)?.value.toString()
                                    binding.highest52Price.text =
                                        korStockDetailInfos?.get(8)?.value.toString()
                                    binding.lowPrice.text =
                                        korStockDetailInfos?.get(3)?.value.toString()
                                    binding.lowest52Price.text =
                                        korStockDetailInfos?.get(9)?.value.toString()
                                    //binding.prevPrice.text =
                                    //    korStockDetailInfos?.get(0)?.value.toString()
                                    binding.pbr.text =
                                        korStockDetailInfos?.get(14)?.value.toString()
                                    binding.per.text =
                                        korStockDetailInfos?.get(10)?.value.toString()
                                    binding.eps.text =
                                        korStockDetailInfos?.get(11)?.value.toString()
                                }
                                count += 1
                                delay(1000)

                            }
                        } else {

                            //미국 차트
                            val usdStockChart = retrofitInstanceUsdStock.api.getUsdChart(reutersCode)
                            val usdStockChartBody = usdStockChart.body()

                            initChart()
                            if (usdStockChartBody != null) {
                                setChartData(usdStockChartBody)
                            }

                            //미국 상세
                            runOnUiThread {
                                binding.stockName.text = koreanName
                            }
                            var count = 0
                            // 3-1. 표에 뿌리기
                            while (true) {
                                val usdStockDetail = retrofitInstanceUsdStock.api.getUsdStock(reutersCode)
                                val usdStockDetailBody = usdStockDetail.body()
                                val usdStockDetailInfos = usdStockDetailBody?.stockItemTotalInfos
                                

                                runOnUiThread {

                                    // UI변경 부분을 입력하자
                                    binding.stockPrice.text =
                                        usdStockDetailBody?.closePrice.toString()
                                    binding.stockPercent.text =
                                        usdStockDetailBody?.fluctuationsRatio.toString() + "%"
                                    binding.openingPrice.text =
                                        usdStockDetailInfos?.get(1)?.value.toString()
                                    binding.dividend.text =
                                        usdStockDetailInfos?.get(15)?.value.toString()
                                    binding.highPrice.text =
                                        usdStockDetailInfos?.get(2)?.value.toString()
                                    binding.highest52Price.text =
                                        usdStockDetailInfos?.get(8)?.value.toString()
                                    binding.lowPrice.text =
                                        usdStockDetailInfos?.get(3)?.value.toString()
                                    binding.lowest52Price.text =
                                        usdStockDetailInfos?.get(9)?.value.toString()
                               //     binding.prevPrice.text =
                               //         usdStockDetailInfos?.get(0)?.value.toString()
                                    binding.pbr.text =
                                        usdStockDetailInfos?.get(12)?.value.toString()
                                    binding.per.text =
                                        usdStockDetailInfos?.get(10)?.value.toString()
                                    binding.eps.text =
                                        usdStockDetailInfos?.get(11)?.value.toString()
                                }
                                count += 1
                                delay(1000)

                            }


                        }
                        // 한투api 너무 에러 많이나서 그냥 다 네이버로 통일함.





                    }





                } catch (e:Exception) {
                    e.printStackTrace()
                }



            }
            // 3. 이게 한국주식이면 한투 API를 탈거고 아니면 네이버 API를 탄다.

            // 4. 각각의 API에서 데이터들(차트, 세부정보) 가져와서 뿌린다.

            // 5. 저 4번을 반복한다. 구조는 CRYPTO랑 똑같이이

        }

        binding.getVoiceTextButton.setOnClickListener {
            VoiceService.instance.getVoiceText(listener = object : GenieSdkEventListener {
                override fun callback(result: Response) {
                    Log.d("testing", "* resultCode: ${result.resultCode}\n* resultMsg: ${result.resultMsg}\n* extra: ${result.extra}")

                    if(result.resultCode == 200) {
                        var resultTest = result.extra.get("sttResult").toString().replace("\"","")
                        Log.d("testing", resultTest)
                        if(resultTest.contains("화면")) {
                            binding.buttonCrypto.callOnClick()
                        } else if (resultTest.contains("종료") || resultTest.contains("꺼줘")){
                            jobDashboard?.cancel()
                            jobSearch?.cancel()
                            finish()
                        } else if(resultTest.contains("검색")){
                            resultTest = resultTest.split("검색")[0].replace(" ","")
                            binding.editStock.setText(resultTest)
                            binding.searchStock.callOnClick()
                        } else {
                            binding.editStock.setText(resultTest)
                            binding.searchStock.callOnClick()
                        }

                    } else {
                        Toast.makeText(applicationContext,result.resultCode.toString(), Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext,"다시 음성인식 해주세요", Toast.LENGTH_SHORT).show()
                    }

                }
            })

        }
        // crypto로 화면전환
        binding.buttonCrypto.setOnClickListener{
            jobDashboard?.cancel()
            jobSearch?.cancel()
            val nextIntent = Intent(this, CryptoActivity::class.java)
            startActivity(nextIntent)
            finish()

        }

        // 권한 승인에 관한 내용..



    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // example of appInfo: {NE-XXX:YYY}
        val appInfo = intent?.getStringExtra("appInfo")

        // example of uri: yourcustomscheme://yourcustomhost/B-StartApp
        val uri = intent?.data

        // example of path: /B-StartApp
        val path = uri?.path
    }



    private fun initChart() {
        binding.apply {
            priceChart.setMaxVisibleValueCount(200)
            priceChart.setPinchZoom(false)
            priceChart.setDrawGridBackground(false)
            priceChart.description.isEnabled = false

            // x축 설정
            priceChart.xAxis.apply {
                textColor = Color.TRANSPARENT
                position = XAxis.XAxisPosition.BOTTOM
                // 세로선 표시 여부 설정
                this.setDrawGridLines(false)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            // 왼쪽 y축 설정
            priceChart.axisLeft.apply {
                textColor = Color.WHITE
                isEnabled = false
            }
            // 오른쪽 y축 설정
            priceChart.axisRight.apply {
                setLabelCount(7, false)
                textColor = Color.BLACK
                // 가로선 표시 여부 설정
                setDrawGridLines(false)
                // 차트의 오른쪽 테두리 라인 설정
                setDrawAxisLine(true)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            priceChart.legend.isEnabled = false
        }
    }

    // 차트데이터 세팅 함수
    private fun setChartData(candles: UsdStockChart) {
        val priceEntries = ArrayList<CandleEntry>()
        var count = 0
        val candlesLoop = candles.priceInfos
        for (candle in candlesLoop) {
            // 캔들 차트 entry 생성
            priceEntries.add(
                CandleEntry(
                    count.toFloat(),
                    candle.highPrice.toFloat(),
                    candle.lowPrice.toFloat(),
                    candle.openPrice.toFloat(),
                    candle.closePrice.toFloat()
                )
            )
            count+=1
        }

        val priceDataSet = CandleDataSet(priceEntries, "").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            // 심지 부분 설정
            shadowColor = Color.LTGRAY
            shadowWidth = 0.7F
            // 음봉 설정
            decreasingColor = Color.rgb(18, 98, 197)
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉 설정
            increasingColor = Color.rgb(200, 74, 49)
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.rgb(6, 18, 34)
            setDrawValues(false)
            // 터치시 노란 선 제거
            highLightColor = Color.TRANSPARENT
        }

        binding.priceChart.apply {
            this.data = CandleData(priceDataSet)
            invalidate()
        }
    }

    private fun setChartDataKor(candles: KorStockChart) {
        val priceEntries = ArrayList<CandleEntry>()
        var count = 0
        val candlesLoop = candles.priceInfos
        for (candle in candlesLoop) {
            // 캔들 차트 entry 생성
            priceEntries.add(
                CandleEntry(
                    count.toFloat(),
                    candle.highPrice.toFloat(),
                    candle.lowPrice.toFloat(),
                    candle.openPrice.toFloat(),
                    candle.closePrice.toFloat()
                )
            )
            count+=1
        }

        val priceDataSet = CandleDataSet(priceEntries, "").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            // 심지 부분 설정
            shadowColor = Color.LTGRAY
            shadowWidth = 0.7F
            // 음봉 설정
            decreasingColor = Color.rgb(18, 98, 197)
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉 설정
            increasingColor = Color.rgb(200, 74, 49)
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.rgb(6, 18, 34)
            setDrawValues(false)
            // 터치시 노란 선 제거
            highLightColor = Color.TRANSPARENT
        }

        binding.priceChart.apply {
            this.data = CandleData(priceDataSet)
            invalidate()
        }


    }


}