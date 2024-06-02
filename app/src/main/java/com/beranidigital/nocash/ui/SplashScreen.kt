package com.beranidigital.nocash.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.beranidigital.nocash.databinding.ActivitySplashScreenBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import kotlinx.coroutines.*
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val delayMs: Long = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        CoroutineScope(Dispatchers.Main).launch {
            delay(delayMs)
            setupSplashScreen()
        }

//        Handler().postDelayed({
//            setupSplashScreen()
//        }, delayMs)
    }

    private fun checkOnBoarding(): Boolean{
        val sharePref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharePref.getBoolean("Finished", false)
    }

    private fun setupSplashScreen(){
        if(checkOnBoarding()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }
    }
}