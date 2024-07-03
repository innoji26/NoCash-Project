package com.beranidigital.nocash.ui.shop.seller.lunas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.databinding.ActivitySellerLunasBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailPiutangActivity
import com.beranidigital.nocash.ui.shop.seller.piutang.SellerPiutangAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SellerLunasActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerLunasBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SellerLunasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerLunasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur padding untuk status bar dan navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengatur toolbar back button
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        recyclerView = binding.rvSellerLunas
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        fetchPiutangData()
    }

    private fun fetchPiutangData() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Logging untuk memastikan userId didapatkan dengan benar
        Log.d("SellerLunasActivity", "User ID: $userId")

        // Query untuk mencari hutang berdasarkan debtorId
        val query = dbRef.child("debts").orderByChild("creditorId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                val debtIdMap = mutableMapOf<DebtsModel, String>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Lunas" && debt.userAgree == true) {
                        debtList.add(debt)
                        debtIdMap[debt] = debtSnapshot.key ?: ""
                    }
                }

                // Logging untuk memastikan data hutang didapatkan dengan benar
                Log.d("SellerLunasActivity", "Jumlah hutang lunas: ${debtList.size}")

                if (debtList.isNotEmpty()) {
                    binding.rvSellerLunas.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.rvSellerLunas.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = SellerLunasAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: SellerLunasAdapter.OnItemClickCallback{
                    override fun onItemClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt]
                        val intent = Intent(this@SellerLunasActivity, DetailPiutangActivity::class.java)
                        intent.putExtra(DetailPiutangActivity.EXTRA_ID_DEBTS_PIUTANG, debtId)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(this@SellerLunasActivity, "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()
                Log.e("SellerLunasActivity", "Database error: ${error.message}")
            }
        })
    }
}
