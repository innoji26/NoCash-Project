package com.beranidigital.nocash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityScanKtpBinding

class ScanKtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanKtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}