package com.beranidigital.nocash.ui.shop.buyer.lunas

import android.content.Intent
import android.os.Bundle
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
import com.beranidigital.nocash.databinding.ActivityBuyerLunasBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.beranidigital.nocash.ui.shop.buyer.hutang.BuyerHutangAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BuyerLunasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyerLunasBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BuyerLunasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerLunasBinding.inflate(layoutInflater)
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

        recyclerView = binding.rvBuyerLunas
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        fetchHutangData()
    }

    private fun fetchHutangData() {
        val userId = auth.currentUser?.uid

        // Query untuk mencari hutang berdasarkan debtorId
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(userId)
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

                if (debtList.isNotEmpty()) {
                    binding.rvBuyerLunas.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.rvBuyerLunas.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = BuyerLunasAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: BuyerLunasAdapter.OnItemClickCallback{
                    override fun onItemClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt]
                        val intent = Intent(this@BuyerLunasActivity, DetailHutangActivity::class.java)
                        intent.putExtra(DetailHutangActivity.EXTRA_ID_DEBTS_HUTANG, debtId)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(this@BuyerLunasActivity, "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()
            }
        })
    }
}