package com.beranidigital.nocash.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityOnBoardingBinding
import com.beranidigital.nocash.ui.otp.OtpActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonListener()
    }

    private fun buttonListener(){
        binding.btnToLogin.setOnClickListener {
            // TODO: navigate to login page
        }
    }
}