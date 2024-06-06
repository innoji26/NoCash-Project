package com.beranidigital.nocash.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityScanKtpBinding
import com.beranidigital.nocash.ui.identitas.DataIdentitasActivity
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity

class ScanKtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanKtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnSave.setOnClickListener {
            val newIntent = Intent(this, DataIdentitasActivity::class.java)
            startActivity(newIntent)
        }
    }


    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}