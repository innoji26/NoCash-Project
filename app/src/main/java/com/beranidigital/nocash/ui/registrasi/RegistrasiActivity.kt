package com.beranidigital.nocash.ui.registrasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityRegistrasiBinding
import com.beranidigital.nocash.ui.otp.OtpActivity

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< HEAD

=======
>>>>>>> 9401669b05d54c9b17b807dec5af2000cec3d824

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnTextLogin.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("type", "register") // set from OTP activity receive data type for switch OTP from register
            startActivity(intent)
        }
    }

<<<<<<< HEAD
//    private fun spinnerStat() {
//        val spinnerButton = binding.spinner
//        val itemStat = resources.getStringArray(R.array.User)
//        if (spinnerButton != null) {
//            val adapter = ArrayAdapter(this, R.layout.item_spinner, itemStat)
//            spinnerButton.adapter = adapter
//            spinnerButton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    p0: AdapterView<*>?,
//                    p1: View?,
//                    possition: Int,
//                    p3: Long,
//                ) {
//                    Toast.makeText(
//                        this@RegistrasiActivity,
//                        getString(R.string.selected_item) + "" + "" + itemStat[possition],
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                }
//            }
//        }
//    }
=======

>>>>>>> 9401669b05d54c9b17b807dec5af2000cec3d824
}