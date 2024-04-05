package com.beranidigital.nocash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityDetailPromoBinding

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPromoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}