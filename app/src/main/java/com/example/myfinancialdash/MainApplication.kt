package com.example.myfinancialdash

import android.app.Application
import android.util.Log
import com.kt.gigagenie.geniesdk.GenieSdk
import com.kt.gigagenie.geniesdk.GenieSdkEventListener
import com.kt.gigagenie.geniesdk.data.model.Response

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GenieSdk.init(
            this,
            "N5004418",
            "TjUwMDQ0MTh8R0JPWERFVk18MTY1MjA4MDExMDY4Nw==",
            true,
            object : GenieSdkEventListener {
                override fun callback(result: Response) {
                    Log.i("[GenieSDKSample]", "result: $result")
                }
            }
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("testing", "여기 오냐?")
        GenieSdk.deinit()
    }
}