package com.beranidigital.nocash.ui.otp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityOtpBinding
import com.google.android.material.snackbar.Snackbar

class OtpActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val numberDump = "+6281234567890"
        binding.tvNumberPhone.text = String.format(resources.getString(R.string.send_code_to_number), numberDump)

        binding.tvResend.setOnClickListener {
            Snackbar.make(binding.root, "Resend code", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnContinue.setOnClickListener {
            Snackbar.make(binding.root, "Verify code", Snackbar.LENGTH_SHORT).show()
            // TODO: navigate to next page
        }
    }


}