package com.beranidigital.nocash.ui.registrasi

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityRegistrasiBinding

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

   private fun spinnerStat(){
        val spinnerButton=binding.spinner
       val itemStat=resources.getStringArray(R.array.User)
       if (spinnerButton!=null){
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,itemStat)
           spinnerButton.adapter= adapter
       }
    }
}