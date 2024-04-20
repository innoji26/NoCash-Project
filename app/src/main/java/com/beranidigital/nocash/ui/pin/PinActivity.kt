package com.beranidigital.nocash.ui.pin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)

    }
}