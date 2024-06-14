package com.beranidigital.nocash.ui.registrasi

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.databinding.ActivityRegistrasiBinding
import com.beranidigital.nocash.ui.otp.OtpActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {

        }
    }
 private fun registerActivity(email: String, password: String){
     auth= Firebase.auth
     auth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(this) { task ->
             if (task.isSuccessful) {
                 // Sign in success, update UI with the signed-in user's information
                 Log.d(TAG, "createUserWithEmail:success")
                 val user = auth.currentUser
//                 updateUI(user)
             } else {
                 // If sign in fails, display a message to the user.
                 Log.w(TAG, "createUserWithEmail:failure", task.exception)
                 Toast.makeText(
                     baseContext,
                     "Authentication failed.",
                     Toast.LENGTH_SHORT,
                 ).show()
//                 updateUI(null)
             }
         }

 }
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

}