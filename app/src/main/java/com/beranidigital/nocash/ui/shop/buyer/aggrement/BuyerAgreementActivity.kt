package com.beranidigital.nocash.ui.shop.buyer.aggrement

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
import com.beranidigital.nocash.databinding.ActivityBuyerAgreementBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BuyerAgreementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyerAgreementBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BuyerAgreementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerAgreementBinding.inflate(layoutInflater)
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

        recyclerView = binding.rvBuyerAgreement
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth


        fetchHutangData()
    }

    private fun fetchHutangData() {
        val userId = auth.currentUser?.uid
//        var debtId: String? = ""

        // Query untuk mencari hutang berdasarkan debtorId
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                val debtIdMap = mutableMapOf<DebtsModel, String>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == false) {
                        debtIdMap[debt] = debtSnapshot.key ?: ""
                        debtList.add(debt)
                    }
                }

                if (debtList.isNotEmpty()) {
                    binding.rvBuyerAgreement.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.rvBuyerAgreement.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = BuyerAgreementAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: BuyerAgreementAdapter.OnItemClickCallback{
                    override fun onItemClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt]
                        val intent = Intent(this@BuyerAgreementActivity, DetailHutangActivity::class.java)
                        intent.putExtra(DetailHutangActivity.EXTRA_ID_DEBTS_HUTANG, debtId)
                        startActivity(intent)
                    }

                    override fun onApproveClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt] ?: return
                        approveDebt(debtId)
                    }

                    override fun onRejectClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt] ?: return
                        rejectDebt(debtId)
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(this@BuyerAgreementActivity, "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun approveDebt(debtId: String) {
        dbRef.child("debts").child(debtId).child("userAgree").setValue(true)
            .addOnSuccessListener {
                Toast.makeText(this, "Hutang disetujui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyetujui hutang", Toast.LENGTH_SHORT).show()
            }
    }

    private fun rejectDebt(debtId: String) {
        dbRef.child("debts").child(debtId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Hutang ditolak", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menolak hutang", Toast.LENGTH_SHORT).show()
            }
    }
}