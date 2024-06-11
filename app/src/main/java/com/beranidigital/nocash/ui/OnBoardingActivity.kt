package com.beranidigital.nocash.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityOnBoardingBinding
import com.beranidigital.nocash.ui.login.LoginActivity

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
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            onBoardingFinished()
        }

    }

    private fun onBoardingFinished(){
        val sharePref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharePref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}