package com.beranidigital.nocash.ui.hutangPiutang.add

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.databinding.ActivityAddHutangBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHutangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHutangBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private var debtorId: String? = null
    private lateinit var edtJatuhTempo: TextInputEditText
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddHutangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        initUI()

        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        binding.btnChekcUser.setOnClickListener{
            val phone = binding.edtPhone.text.toString()
            if(phone.isNotEmpty()){
                checkCreditorByPhone(phone)

            }else{
                Toast.makeText(this@AddHutangActivity, "Please enter phone number", Toast.LENGTH_SHORT).show()
            }
        }

        edtJatuhTempo = findViewById(R.id.edtJatuhTempo)
        edtJatuhTempo.setOnClickListener { showDatePicker() }
    }

    private fun checkCreditorByPhone(phoneUser: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val snapshot = database.getReference("users").orderByChild("phone").equalTo(phoneUser).get().await()

                if(snapshot.exists()){
                    val userSnapshot = snapshot.children.iterator().next()
                    debtorId = userSnapshot.key
                    Toast.makeText(this@AddHutangActivity, "User ditemukan", Toast.LENGTH_SHORT).show()

                    Log.e(TAG, "Check Creditor $debtorId")
                }else{
                    debtorId = null
                    Toast.makeText(this@AddHutangActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                debtorId = null
                Toast.makeText(this@AddHutangActivity, "Failed to check User", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to check User", e)
            }
        }
    }

    private fun addDebtData(debt: DebtsModel){
        val currentUserId = auth.currentUser?.uid
        CoroutineScope(Dispatchers.Main).launch {
            if(currentUserId != null){
                try {
                    database.getReference("debts").push().setValue(debt).await()
//                    crediturId?.let { database.getReference("users").child(it).child("debts").push().setValue(debt).await() }
                    Toast.makeText(this@AddHutangActivity, "Success save Debt data", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddHutangActivity, MainHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }catch (e: Exception){
                    Toast.makeText(this@AddHutangActivity, "Failed to save Debt data", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@AddHutangActivity, "Please Login first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUI(){
        binding.button.setOnClickListener {
            val jumlahHutang = binding.edtTotalHutang.text.toString().trim()
            val description = binding.edtDesc.text.toString().trim()
            val jatuhTempo = binding.edtJatuhTempo.text.toString().trim()

            val currentUser = auth.currentUser?.uid

            if(debtorId == null){
                Toast.makeText(this, "Please Check first", Toast.LENGTH_SHORT).show()
            }else if (currentUser != null && jumlahHutang.isNotEmpty() && description.isNotEmpty() && jatuhTempo.isNotEmpty()) {
                val debt = DebtsModel(debtorId!!, currentUser,  jumlahHutang, description, jatuhTempo)
                addDebtData(debt)
            }else{
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Set calendar to selected date
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Format the selected date and set it to TextInputEditText
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)
                edtJatuhTempo.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Optional: Set minimum date
        datePickerDialog.show()
    }

    companion object{
        private const val TAG = "AddHutangActivity"
    }
}