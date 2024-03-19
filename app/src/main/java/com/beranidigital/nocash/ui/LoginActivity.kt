package com.beranidigital.nocash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


    }
}