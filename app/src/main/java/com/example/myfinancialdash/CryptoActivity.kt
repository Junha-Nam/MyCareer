package com.example.myfinancialdash

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.myfinancialdash.api.RetrofitInstance_Crypto
import com.example.myfinancialdash.api.RetrofitInstance_Dollar
import com.example.myfinancialdash.api.RetrofitInstance_IndexStock
import com.example.myfinancialdash.data.cryptochart.CryptoChart
import com.example.myfinancialdash.databinding.ActivityCryptoBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.kt.gigagenie.geniesdk.GenieSdkEventListener
import com.kt.gigagenie.geniesdk.data.model.Response
import com.kt.gigagenie.geniesdk.service.VoiceService
import kotlinx.coroutines.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round
import kotlin.math.roundToInt


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

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        jobDashboard = CoroutineScope(Dispatchers.IO).launch {
            try {
                while(true){
                    // 값을 가져오고
                    val usdIndexSearch = RetrofitInstance_IndexStock.api.getUsdIndex()
                    val dollarIndexSearch = RetrofitInstance_Dollar.api.getIndexDollar()
                    val cryptoKorBit = retrofitDashboard.api.getCryptoDetail("KRW-BTC")
                    val cryptoUsdBit = retrofitDashboard.api.getCryptoDetail("USDT-BTC")
                    val cryptoKorEth = retrofitDashboard.api.getCryptoDetail("KRW-ETH")
                    val cryptoUsdEth = retrofitDashboard.api.getCryptoDetail("USDT-ETH")
                    val cryptoKorDog = retrofitDashboard.api.getCryptoDetail("KRW-DOGE")
                    val cryptoUsdDog = retrofitDashboard.api.getCryptoDetail("USDT-DOGE")

                    // data에서 필요부분만 뽑아내기

                    val usdIndexSearchBody = usdIndexSearch.body()?.datas
                    val dollarIndexSearchBody = dollarIndexSearch.body()
                    //Body를 저장해서
                    val cryptoKorBitBody = cryptoKorBit.body()
                    val cryptoUsdBitBody = cryptoUsdBit.body()
                    val cryptoKorEthBody = cryptoKorEth.body()
                    val cryptoUsdEthBody = cryptoUsdEth.body()
                    val cryptoKorDogBody = cryptoKorDog.body()
                    val cryptoUsdDogBody = cryptoUsdDog.body()

                    // snp500 나스닥 다우존스
                    if (usdIndexSearchBody != null) {
                        for (i in usdIndexSearchBody) {
                            if (i.reutersCode == ".IXIC") {
                                runOnUiThread {
                                    binding.NasdaqPrice.text = i.closePrice
                                    binding.NasdaqRate.text = i.fluctuationsRatio
                                }
                                break
                            }
                        }
                    }

                    // 달러

                    if (dollarIndexSearchBody != null) {
                        for (i in dollarIndexSearchBody) {
                            if (i.symbolCode == "USD") {
                                runOnUiThread {
                                    binding.dollarPrice.text = i.closePrice
                                    binding.dollarRate.text = i.fluctuationsRatio
                                }
                                break
                            }
                        }
                    }




                    //price와 rate를 뿌려준다.
                    runOnUiThread {
                        binding.krwBtcPrice.text = toDoubleFormat(cryptoKorBitBody?.get(0)?.trade_price.toString().toDouble())
                        binding.krwBtcRate.text =
                            df.format(cryptoKorBitBody?.get(0)?.signed_change_rate?.times(100))+"%"
                        binding.krwEthPrice.text = toDoubleFormat(cryptoKorEthBody?.get(0)?.trade_price.toString().toDouble())
                        binding.krwEthRate.text =
                            df.format(cryptoKorEthBody?.get(0)?.signed_change_rate?.times(100))+"%"
                        binding.krwDogPrice.text = toDoubleFormat(cryptoKorDogBody?.get(0)?.trade_price.toString().toDouble())
                        binding.krwDogRate.text =
                            df.format(cryptoKorDogBody?.get(0)?.signed_change_rate?.times(100))+"%"

                        binding.usdBtcPrice.text = toDoubleFormat(cryptoUsdBitBody?.get(0)?.trade_price.toString().toDouble())
                        binding.usdBtcRate.text =
                                    df.format(cryptoUsdBitBody?.get(0)?.signed_change_rate?.times(100))+"%"
                        binding.usdEthPrice.text = toDoubleFormat(cryptoUsdEthBody?.get(0)?.trade_price.toString().toDouble())
                        binding.usdEthRate.text =
                                    df.format(cryptoUsdEthBody?.get(0)?.signed_change_rate?.times(100))+"%"
                        binding.usdDogPrice.text = toDoubleFormat(cryptoUsdDogBody?.get(0)?.trade_price.toString().toDouble())
                        binding.usdDogRate.text =
                                    df.format(cryptoUsdDogBody?.get(0)?.signed_change_rate?.times(100))+"%"
                    }

                    countTest += 1

                    //CryptoData().cryptoIndex(retrofitDashboard)

                    delay(1000)
                }


            } catch(e:Exception) {
                e.printStackTrace()

            }

        }


        binding.searchCrpyto.setOnClickListener {
            jobSearch?.cancel()
            val searchName = binding.editCrypto.text.toString()
            var koreanName = searchName

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
                            if (i.korean_name == koreanName && i.market.contains("KRW")) {
                                println(i.market)
                                searchMarket = i.market
                                koreanName = i.korean_name
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
                        runOnUiThread {
                            binding.cryptoName.text = koreanName
                        }
                        var count = 0
                        // 3-1. 표에 뿌리기
                        while (true) {
                            val cryptoDetail =
                                retrofitCryptoSearch.api.getCryptoDetail(searchMarket)
                            val cryptoDetailBody = cryptoDetail.body()

                            runOnUiThread {
                                // UI변경 부분을 입력하자
                                binding.cryptoPrice.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.trade_price.toString().toDouble())
                                binding.cryptoPercent.text =
                                    df.format(cryptoDetailBody?.get(0)?.signed_change_rate?.times(100))+"%"

                                binding.openingPrice.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.opening_price.toString().toDouble())
                                binding.highest52Date.text =
                                    cryptoDetailBody?.get(0)?.highest_52_week_date.toString()
                                binding.highPrice.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.high_price.toString().toDouble())
                                binding.highest52Price.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.highest_52_week_price.toString().toDouble())
                                binding.lowPrice.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.low_price.toString().toDouble())
                                binding.lowest52Date.text =
                                    cryptoDetailBody?.get(0)?.lowest_52_week_date.toString()
                                binding.prevPrice.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.prev_closing_price.toString().toDouble())
                                binding.lowest52Price.text =
                                    toDoubleFormat(cryptoDetailBody?.get(0)?.lowest_52_week_price.toString().toDouble())
                            }

                            count += 1
                            delay(1000)

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
            finish()

        }

        binding.getVoiceTextButton.setOnClickListener {

            VoiceService.instance.getVoiceText(listener = object : GenieSdkEventListener {
                override fun callback(result: Response) {
                    Log.d("testing", "* resultCode: ${result.resultCode}\n* resultMsg: ${result.resultMsg}\n* extra: ${result.extra}")

                    if(result.resultCode == 200) {
                        var resultTest = result.extra.get("sttResult").toString().replace("\"","")
                        Log.d("testing", resultTest)
                        if(resultTest.contains("화면")) {
                            binding.buttonStock.callOnClick()
                        } else if (resultTest.contains("종료") || resultTest.contains("꺼줘")){
                            jobDashboard?.cancel()
                            jobSearch?.cancel()
                            finish()

                        } else if(resultTest.contains("검색")){
                            resultTest = resultTest.split("검색")[0].trim()
                            binding.editCrypto.setText(resultTest)
                            binding.searchCrpyto.callOnClick()
                        } else {
                            binding.editCrypto.setText(resultTest)
                            binding.searchCrpyto.callOnClick()
                        }

                    } else {
                        Toast.makeText(applicationContext,"다시 음성인식 해주세요", Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }
    }

    // 차트 초기화 함수
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
    private fun setChartData(candles: CryptoChart) {
        val priceEntries = ArrayList<CandleEntry>()
        var count = candles.size
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
            count-=1
        }
        priceEntries.reverse()

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

    private fun toDoubleFormat(num: Double): String {
        var df: DecimalFormat?
        if(num >=100 && num <= 999.9) {
            df = DecimalFormat("000.0")
        } else if(num >=10 && num <= 99.9) {
            df = DecimalFormat("00.00")
        } else if(num >=1 && num <= 9.9) {
            df = DecimalFormat("0.000")
        } else if(num <1) {
            df = DecimalFormat("0.00000")
        } else {
            df = DecimalFormat("###,###,###")
        }
        return df.format(num)
    }


}