package com.beranidigital.nocash.ui.complete_register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityCompleteRegiterBinding
import com.beranidigital.nocash.ui.LoginActivity

class CompleteRegiterActivity : AppCompatActivity() {

    lateinit var binding: ActivityCompleteRegiterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteRegiterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNextLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }
}