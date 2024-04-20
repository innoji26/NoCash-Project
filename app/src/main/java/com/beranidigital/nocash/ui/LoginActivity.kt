package com.beranidigital.nocash.ui

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityLoginBinding
import com.beranidigital.nocash.ui.otp.OtpActivity
import com.beranidigital.nocash.ui.registrasi.RegistrasiActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, OtpActivity::class.java))
        }

        val btnTextRegister = binding.btnTextRegister
        btnTextRegister.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }

        binding.forgotPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}