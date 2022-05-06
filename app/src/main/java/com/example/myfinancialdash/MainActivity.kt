
package com.example.myfinancialdash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
//import com.example.myfinancialdash.api.RetrofitInstance
import com.example.myfinancialdash.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : FragmentActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

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


        //



        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                //val retrofitTest = RetrofitInstance

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

        // 우측 내용 실시간 표시
        // coroutine 을 사용

        // Idea 1. 세부 내역 검색을 누른순간 EditText에 있는 값을 변수 하나에 놔두고
        // 이 변수가 ""이 아니면 이 부분도 코루틴에서 반복을 수행한다.
        // 어디까지 가져올거냐.. 가 문젠데

        //

        // 버튼 클릭 시. 코루틴 비동기 처리
        // 세부내역 표시(좌측화면)
       binding.searchStock.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                try {






                } catch (e:Exception) {
                    e.printStackTrace()
                }



            }
        }

        // 검색버튼 클릭 시 동작.
        binding.searchStock.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                try {






                } catch (e:Exception) {
                    e.printStackTrace()
                }



            }
        }

        // crypto로 화면전환
        binding.buttonCrypto.setOnClickListener{
            job.cancel()
            val nextIntent = Intent(this, CryptoActivity::class.java)
            startActivity(nextIntent)
            println("넘어가도 동작해?")
            finish()
            println("stop이후도 동작해?")



        }

        // 권한 승인에 관한 내용..

    }
}