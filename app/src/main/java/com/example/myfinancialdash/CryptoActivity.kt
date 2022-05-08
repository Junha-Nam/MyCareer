package com.example.myfinancialdash

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.myfinancialdash.api.RetrofitInstance_Crypto
import com.example.myfinancialdash.data.cryptochart.CryptoChart
import com.example.myfinancialdash.databinding.ActivityCryptoBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.*


class CryptoActivity : FragmentActivity() {

    val binding by lazy { ActivityCryptoBinding.inflate(layoutInflater) }

    private var jobSearch: Job? = null
    private var jobDashboard: Job? =null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root )

        
        // 여기는 우측 대시보드. 즉, 실시간으로 계속 새로고침? 갱신 이 되어야 하는 곳이야. 
        var countTest = 0
        val retrofitDashboard = RetrofitInstance_Crypto
        jobDashboard = CoroutineScope(Dispatchers.IO).launch {
            try {
                while(true){
                    // 값을 가져오고
                    val cryptoKorBit = retrofitDashboard.api.getCryptoDetail("KRW-BTC")
                    val cryptoUsdBit = retrofitDashboard.api.getCryptoDetail("USDT-BTC")
                    val cryptoKorEth = retrofitDashboard.api.getCryptoDetail("KRW-ETH")
                    val cryptoUsdEth = retrofitDashboard.api.getCryptoDetail("USDT-ETH")
                    val cryptoKorDog = retrofitDashboard.api.getCryptoDetail("KRW-DOGE")
                    val cryptoUsdDog = retrofitDashboard.api.getCryptoDetail("USDT-DOGE")

                    //Body를 저장해서
                    val cryptoKorBitBody = cryptoKorBit.body()
                    val cryptoUsdBitBody = cryptoUsdBit.body()
                    val cryptoKorEthBody = cryptoKorEth.body()
                    val cryptoUsdEthBody = cryptoUsdEth.body()
                    val cryptoKorDogBody = cryptoKorDog.body()
                    val cryptoUsdDogBody = cryptoUsdDog.body()

                    //price와 rate를 뿌려준다.
                    binding.krwBtcPrice.text = cryptoKorBitBody?.get(0)?.trade_price.toString()
                    binding.krwBtcRate.text = cryptoKorBitBody?.get(0)?.signed_change_rate.toString()
                    binding.krwEthPrice.text = cryptoKorEthBody?.get(0)?.trade_price.toString()
                    binding.krwEthRate.text = cryptoKorEthBody?.get(0)?.signed_change_rate.toString()
                    binding.krwDogPrice.text = cryptoKorDogBody?.get(0)?.trade_price.toString()
                    binding.krwDogRate.text = cryptoKorDogBody?.get(0)?.signed_change_rate.toString()

                    binding.usdBtcPrice.text = cryptoUsdBitBody?.get(0)?.trade_price.toString()
                    binding.usdBtcRate.text = cryptoUsdBitBody?.get(0)?.signed_change_rate.toString()
                    binding.usdEthPrice.text = cryptoUsdEthBody?.get(0)?.trade_price.toString()
                    binding.usdEthRate.text = cryptoUsdEthBody?.get(0)?.signed_change_rate.toString()
                    binding.usdDogPrice.text = cryptoUsdDogBody?.get(0)?.trade_price.toString()
                    binding.usdDogRate.text = cryptoUsdDogBody?.get(0)?.signed_change_rate.toString()

                    countTest += 1
                    binding.loopTest.text = countTest.toString()

                    //CryptoData().cryptoIndex(retrofitDashboard)

                    delay(3000)
                }


            } catch(e:Exception) {
                e.printStackTrace()

            }

        }


        binding.searchCrpyto.setOnClickListener {
            jobSearch?.cancel()

            val search_name = binding.editCrypto.text.toString()
            var korean_name = ""
            if(search_name == "a") {
                korean_name = "비트코인"
            } else if (search_name == "b") {
                korean_name = "이더리움"
            }
            val retrofitCryptoSearch = RetrofitInstance_Crypto
            var searchMarket = ""
            jobSearch = CoroutineScope(Dispatchers.IO).launch {
                try {
                    delay(500)
                    // 1. 한글명으로 MarketCode 찾기
                    val cryptoSearch = retrofitCryptoSearch.api.getCryptoSearch()
                    val cryptoSearchBody = cryptoSearch.body()

                    var isTrue = 0
                    if (cryptoSearchBody != null) {
                        for (i in cryptoSearchBody) {
                            if (i.korean_name == korean_name && i.market.contains("KRW")) {
                                println(i.market)
                                searchMarket = i.market
                                korean_name = i.korean_name
                                isTrue = 1
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

                        // 2. 차트정보 가져와 차트그리기
                        val cryptoChart = retrofitCryptoSearch.api.getCryptoChart(searchMarket, 100)
                        val cryptoChartBody = cryptoChart.body()

                        initChart()
                        if (cryptoChartBody != null) {
                            setChartData(cryptoChartBody)
                        }

                        // 2-1. 차트 그리기

                        // 3. 세부정보 가져와 표에 뿌리기
                        // 여기 부분이 무한루프가 돌아야됨. 재 검색 버튼을 누르거나, 화면이 넘어갈때 초기화.

                        binding.cryptoName.text = korean_name
                        var count = 0
                        // 3-1. 표에 뿌리기
                        while (true) {
                            val cryptoDetail =
                                retrofitCryptoSearch.api.getCryptoDetail(searchMarket)
                            val cryptoDetailBody = cryptoDetail.body()

                            runOnUiThread {
                                // UI변경 부분을 입력하자
                                binding.cryptoPrice.text =
                                    cryptoDetailBody?.get(0)?.trade_price.toString()
                                binding.cryptoPercent.text =
                                    cryptoDetailBody?.get(0)?.signed_change_rate.toString()

                                binding.openingPrice.text =
                                    cryptoDetailBody?.get(0)?.opening_price.toString()
                                binding.highest52Date.text =
                                    cryptoDetailBody?.get(0)?.highest_52_week_date.toString()
                                binding.highPrice.text =
                                    cryptoDetailBody?.get(0)?.high_price.toString()
                                binding.highest52Price.text =
                                    cryptoDetailBody?.get(0)?.highest_52_week_price.toString()
                                binding.lowPrice.text =
                                    cryptoDetailBody?.get(0)?.low_price.toString()
                                binding.lowest52Date.text =
                                    cryptoDetailBody?.get(0)?.lowest_52_week_date.toString()
                                binding.prevPrice.text =
                                    cryptoDetailBody?.get(0)?.prev_closing_price.toString()
                                binding.lowest52Price.text =
                                    cryptoDetailBody?.get(0)?.lowest_52_week_price.toString()
                            }

                            count += 1
                            binding.loopTest2.text = count.toString()
                            delay(3000)

                        }
                    }
                } catch(e:Exception) {
                    e.printStackTrace()

                }

            }

        }

        binding.buttonStock.setOnClickListener{
            jobSearch?.cancel()
            jobDashboard?.cancel()
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)

        }
    }

    // 차트 초기화 함수
    fun initChart() {
        binding.apply {
            priceChart.setMaxVisibleValueCount(200)
            priceChart.setPinchZoom(false)
            priceChart.setDrawGridBackground(false)
            // x축 설정
            priceChart.xAxis.apply {
                textColor = Color.TRANSPARENT
                position = XAxis.XAxisPosition.BOTTOM
                // 세로선 표시 여부 설정
                this.setDrawGridLines(true)
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
                textColor = Color.WHITE
                // 가로선 표시 여부 설정
                setDrawGridLines(true)
                // 차트의 오른쪽 테두리 라인 설정
                setDrawAxisLine(true)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            priceChart.legend.isEnabled = false
        }
    }

    // 차트데이터 세팅 함수
    fun setChartData(candles: CryptoChart) {
        val priceEntries = ArrayList<CandleEntry>()
        var count = 0
        for (candle in candles) {
            // 캔들 차트 entry 생성
            priceEntries.add(
                CandleEntry(
                    count.toFloat(),
                    candle.high_price.toFloat(),
                    candle.low_price.toFloat(),
                    candle.opening_price.toFloat(),
                    candle.trade_price.toFloat()
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