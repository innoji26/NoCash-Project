package com.beranidigital.nocash.ui.identitas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityDataIdentitasBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataIdentitasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataIdentitasBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataIdentitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        databaseReference = database.getReference("users")

        binding.button.setOnClickListener {
            saveUserData()
        }
    }

    private fun saveUserData(){
        val name = binding.edtNama.text.toString().trim()
        val nik = binding.edtNik.text.toString().trim()
        val gender = binding.edtGender.text.toString().trim()
        val ttl = binding.edtDateOfBirth.text.toString().trim()
        val address = binding.edtAlamat.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val email = binding.edtemail.text.toString().trim()

        if (name.isEmpty() || nik.isEmpty() || gender.isEmpty() || ttl.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val user = UsersModel(name, nik, gender, ttl, address, phone, email)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    databaseReference.child(userId).setValue(user).await()
                    Toast.makeText(this@DataIdentitasActivity, "User data saved", Toast.LENGTH_SHORT).show()
                    val newIntent =
                        Intent(this@DataIdentitasActivity, MainHomeActivity::class.java)
                    startActivity(newIntent)
                } catch (e: Exception) {
                    Toast.makeText(this@DataIdentitasActivity, "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
