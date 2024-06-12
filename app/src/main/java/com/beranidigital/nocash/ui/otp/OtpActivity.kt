package com.beranidigital.nocash.ui.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.MainActivity
import com.beranidigital.nocash.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class OtpActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private var countdownTimer: CountDownTimer? = null

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

        firebaseAuth = FirebaseAuth.getInstance()

        storedVerificationId = intent.getStringExtra("verificationId")!!
        resendingToken = intent.getParcelableExtra("resendingToken")!!
        val phoneNumber = intent.getStringExtra("phoneNumber")!!

        startCountdown()

        binding.tvNumberPhone.text = phoneNumber

        binding.tvResend.setOnClickListener {
            resendVerificationCode(phoneNumber)
        }

        binding.btnContinue.setOnClickListener {

            val otp = binding.etOtp.text.toString().trim()

            if (otp.isEmpty()) {
                Toast.makeText(this, "Masukkan kode OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyVerificationCode(otp)
            }

        }
    }

    private fun startCountdown() {
        binding.tvResend.isEnabled = false
        countdownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvTimer.text = "$seconds detik"
            }

            override fun onFinish() {
                binding.tvResend.isEnabled = true

            }
        }.start()
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendingToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        startCountdown()
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@OtpActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            resendingToken = token
            Toast.makeText(this@OtpActivity, "Kode OTP telah dikirim ulang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                firebaseAuth.signInWithCredential(credential).await()
                startActivity(Intent(this@OtpActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@OtpActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

}