package com.example.myfinancialdash

import com.example.myfinancialdash.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoFunction {

    private var search_Market_Result = ""
    fun searchMarket(korean_name: String): String {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofitTest = RetrofitInstance

                val callTest = retrofitTest.api.getCryptoSearch()
                val test = callTest.body()

                if (test != null) {
                    for (i in test) {
                        if (i.korean_name == korean_name) {
                            if(i.market.contains("KRW")) {
                                println("여기들어옴?")
                                search_Market_Result = i.market
                            }
                        }
                    }
                }

            } catch(e:Exception) {
                e.printStackTrace()

            }

        }
        return search_Market_Result

    }
}