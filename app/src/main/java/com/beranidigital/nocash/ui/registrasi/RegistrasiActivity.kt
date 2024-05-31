package com.beranidigital.nocash.ui.registrasi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityRegistrasiBinding
import com.beranidigital.nocash.ui.pin.PinActivity
import com.beranidigital.nocash.ui.pin.PinType

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        spinnerStat()

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnTextLogin.setOnClickListener {
            val intent = Intent(this, PinActivity::class.java)
            intent.putExtra("type", PinType.CREATE)
            startActivity(intent)
        }
    }

    private fun spinnerStat() {
        val spinnerButton = binding.spinner
        val itemStat = resources.getStringArray(R.array.User)
        if (spinnerButton != null) {
            val adapter = ArrayAdapter(this, R.layout.item_spinner, itemStat)
            spinnerButton.adapter = adapter
            spinnerButton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    possition: Int,
                    p3: Long,
                ) {
                    Toast.makeText(
                        this@RegistrasiActivity,
                        getString(R.string.selected_item) + "" + "" + itemStat[possition],
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }
}