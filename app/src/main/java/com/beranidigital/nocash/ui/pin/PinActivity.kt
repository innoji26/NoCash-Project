package com.beranidigital.nocash.ui.pin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)

    }
}