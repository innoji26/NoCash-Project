package com.beranidigital.nocash.ui.identitas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityDataIdentitasBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity

class DataIdentitasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataIdentitasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataIdentitasBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            val newIntent = Intent(this, MainHomeActivity::class.java)
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(newIntent)
        }
    }
}
