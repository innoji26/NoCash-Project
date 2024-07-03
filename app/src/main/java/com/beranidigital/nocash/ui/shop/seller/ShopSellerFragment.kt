package com.beranidigital.nocash.ui.shop.seller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.FragmentShopSellerBinding
import com.beranidigital.nocash.ui.hutangPiutang.add.AddHutangActivity
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailPiutangActivity
import com.beranidigital.nocash.ui.shop.seller.lunas.SellerLunasActivity
import com.beranidigital.nocash.ui.shop.seller.piutang.SellerPiutangActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ShopSellerFragment : Fragment() {
    private lateinit var binding: FragmentShopSellerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopSellerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvPiutang
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.IvAddHutang.setOnClickListener {
            val intent = Intent(requireActivity(), AddHutangActivity::class.java)
            startActivity(intent)
        }


        setUserData()
        getCountDataPiutang()
        getCountDataPiutangByStatus()
        fetchPiutangData()
        getCountTotalPiutang()
        buttonListener()
    }

    private fun buttonListener(){
        binding.cvLain.setOnClickListener {

        }

        binding.cvLunas.setOnClickListener {
            val intent = Intent(requireActivity(), SellerLunasActivity::class.java)
            startActivity(intent)
        }

        binding.cvPiutang.setOnClickListener {
            val intent = Intent(requireActivity(), SellerPiutangActivity::class.java)
            startActivity(intent)
        }

        binding.cvTransaksi.setOnClickListener {

        }
    }

    private fun setUserData(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userId = auth.uid ?: ""
                dbRef = db.getReference("users").child(userId)

                val snapshot = dbRef.get().await() // Menunggu hasil dari Firebase secara asinkron
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    if (user != null) {
                        binding.tvName.text = user.name
                    }
                } else {
                    Toast.makeText(requireContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCountDataPiutang(){
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("creditorId").equalTo(currentUser)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == true) {
                        debtList.add(debt)
                    }
                }

                // Jumlah data untuk id tertentu
                val countDebts = debtList.size
                binding.tvCountPiutang.text = countDebts.toString()
                Log.d("TAG", "Jumlah piutang untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun fetchPiutangData() {
        val userId = auth.currentUser?.uid

        // Query untuk mencari hutang berdasarkan debtorId
        val query = dbRef.child("debts").orderByChild("creditorId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                val debtIdMap = mutableMapOf<DebtsModel, String>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == true) {
                        debtList.add(debt)
                        debtIdMap[debt] = debtSnapshot.key ?: ""
                    }
                }

                if (debtList.isNotEmpty()) {
                    binding.rvPiutang.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.rvPiutang.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = ShopSellerAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: ShopSellerAdapter.OnItemClickCallback{
                    override fun onItemClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt]
                        val intent = Intent(activity, DetailPiutangActivity::class.java)
                        intent.putExtra(DetailPiutangActivity.EXTRA_ID_DEBTS_PIUTANG, debtId)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun getCountDataPiutangByStatus() {
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("creditorId").equalTo(currentUser)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Lunas" && debt.userAgree == true) {
                        debtList.add(debt)
                    }
                }

                // Jumlah data untuk status tertentu
                val countDebts = debtList.size
                binding.tvCountSukses.text = countDebts.toString()
                Log.d("TAG", "Jumlah piutang dengan status Lunas untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun getCountTotalPiutang(){
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("creditorId").equalTo(currentUser)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalDebtAmount = 0
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == true) {
                        totalDebtAmount += debt.amount.toIntOrNull() ?: 0
                    }
                }

                // Tampilkan total jumlah hutang untuk user tertentu
                binding.tvTotalNominalTransaksi.text = totalDebtAmount.toString()
                Log.d("TAG", "Total Piutang untuk user $currentUser adalah: $totalDebtAmount")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}