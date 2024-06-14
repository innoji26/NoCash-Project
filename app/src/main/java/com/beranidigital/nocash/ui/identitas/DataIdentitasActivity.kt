package com.beranidigital.nocash.ui.identitas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityDataIdentitasBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class DataIdentitasActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDataIdentitasBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDataIdentitasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference

        binding.button.setOnClickListener{
            addDataIdentity()
        }
        }
    private fun addDataIdentity(){
        val name=binding.EdtName.text.toString()
        val numberInduk=binding.EdtNik.text.toString()
        val genderAdd=binding.gender.text.toString()
        val address =binding.edtAlamat.text.toString()
        val dateBorn=binding.edtDateOfBirth.text.toString()
        val email=binding.edtemail.text.toString()
        val imagektp=""
        val imageprofile=""

        if (name.isEmpty()||numberInduk.isEmpty()||genderAdd.isEmpty()||address.isEmpty()||dateBorn.isEmpty()||email.isEmpty()){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user=UsersModel(name,numberInduk,genderAdd,address,dateBorn,email,imagektp,imageprofile)
        database.child("users").child(numberInduk).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show()
            }
    }
    }
