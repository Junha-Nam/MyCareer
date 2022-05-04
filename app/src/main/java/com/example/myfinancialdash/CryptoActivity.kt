package com.example.myfinancialdash

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import com.example.myfinancialdash.databinding.ActivityCryptoBinding

class CryptoActivity : FragmentActivity() {

    val binding by lazy { ActivityCryptoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root )


        binding.buttonStock.setOnClickListener{
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }
    }
}