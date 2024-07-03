package com.beranidigital.nocash.ui.transaksi.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.TransactionModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ActivityAddTransactionBinding
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        fetchHutangData()
        checkNominal()
    }

    private fun fetchHutangData(){
        val getDebtId = intent.getStringExtra(EXTRA_ID_DEBTS_HUTANG_TRANSACTION)!!
        CoroutineScope(Dispatchers.Main).launch {
            try {
                databaseRef = database.getReference("debts").child(getDebtId)
                val snapshot = databaseRef.get().await()
                if(snapshot.exists()){
                    val debt = snapshot.getValue(DebtsModel::class.java)
                    debt?.let {
                       binding.tvPiutangId.text = getDebtId
                        binding.tvTotalHutang.text = it.amount
                        binding.tvSudahDibayar.text = it.totalPaid
                        val sisaHutang = (it.amount.toInt() - (it.totalPaid?.toInt() ?: 0)).toString()
                        binding.tvSisaHutang.text = sisaHutang
                        fetchUserData(it.debtorId){user ->
                            binding.tvName.text = user.name
                            binding.tvPhone.text = user.phone
                        }
                    }

                }
            }catch (e: Exception){
                Log.e("DetailHutangActivity", "DetailHutang:failure", e)
                Toast.makeText(this@AddTransactionActivity, "Gagal memuat data Hutang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserData(userId: String, callback: (UsersModel) -> Unit) {
        val userRef = database.getReference("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UsersModel::class.java)
                if (user != null) {
                    callback(user)
                } else {
                    Toast.makeText(this@AddTransactionActivity, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddTransactionActivity, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addDebtTransactionData(transaction: TransactionModel){
        val debtId = transaction.debtId
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Update Transaction data
                val transactionRef = database.getReference("transactions").push()
                transactionRef.setValue(transaction).await()

                // Update Debt data
                val debtRef = database.getReference("debts").child(debtId)
                val snapshot = debtRef.get().await()
                if (snapshot.exists()) {
                    val debt = snapshot.getValue(DebtsModel::class.java)
                    debt?.let {
                        val newTotalPaid = (it.totalPaid?.toInt() ?: 0) + transaction.amount
                        it.totalPaid = newTotalPaid.toString()

                        // Update status if totalPaid equals the debt amount
                        if (newTotalPaid == it.amount.toInt()) {
                            it.status = "Lunas"
                        }

                        it.updatedAt = System.currentTimeMillis()
                        debtRef.setValue(it).await()
                    }
                    val intent = Intent(this@AddTransactionActivity, MainHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(this@AddTransactionActivity, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("AddTransactionActivity", "addDebtTransactionData:failure", e)
                Toast.makeText(this@AddTransactionActivity, "Gagal menambahkan transaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkNominal(){
        binding.btnBayar.setOnClickListener {
            val getDebtId = intent.getStringExtra(EXTRA_ID_DEBTS_HUTANG_TRANSACTION)!!
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    databaseRef = database.getReference("debts").child(getDebtId)
                    val snapshot = databaseRef.get().await()
                    if (snapshot.exists()) {
                        val debt = snapshot.getValue(DebtsModel::class.java)
                        debt?.let {
                            val nominal = binding.edtNominal.text.toString().toInt() + (it.totalPaid?.toInt() ?: 0)
                            if (nominal > it.amount.toInt()) {
                                Toast.makeText(this@AddTransactionActivity, "Nominal melebihi jumlah Hutang yang harus dibayar", Toast.LENGTH_SHORT).show()
                            } else {
                                // Jika nominal tidak melebihi, tambahkan transaksi
                                val transaction = TransactionModel(
                                    debtId = getDebtId,
                                    amount = binding.edtNominal.text.toString().toInt(),
                                    status = "Paid" // atau status lainnya sesuai kebutuhan
                                )
                                addDebtTransactionData(transaction)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AddTransactionActivity", "fetchHutangData:failure", e)
                    Toast.makeText(this@AddTransactionActivity, "Gagal memuat data Hutang", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        const val EXTRA_ID_DEBTS_HUTANG_TRANSACTION = "extra_id_debts_hutang_transaction"
    }
}