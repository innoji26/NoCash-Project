package com.beranidigital.nocash.ui.registrasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityRegistrasiBinding
import com.beranidigital.nocash.ui.LoginActivity
import com.beranidigital.nocash.ui.otp.OtpActivity

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("type", "register") // set from OTP activity receive data type for switch OTP from register
            startActivity(intent)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}