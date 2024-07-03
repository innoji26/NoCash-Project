package com.beranidigital.nocash.ui.shop.buyer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.FragmentShopBuyerBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.beranidigital.nocash.ui.shop.buyer.aggrement.BuyerAgreementActivity
import com.beranidigital.nocash.ui.shop.buyer.hutang.BuyerHutangActivity
import com.beranidigital.nocash.ui.shop.buyer.lunas.BuyerLunasActivity
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


class ShopBuyerFragment : Fragment() {
    private lateinit var binding: FragmentShopBuyerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopBuyerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvHutang
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()




        setUserData()
        getCountDataHutang()
        fetchHutangData()
        getCountTotalHutang()
        getCountDataHutangByStatus()
        getCountDataHutangByUserAgree()
        buttonListener()
    }

    private fun buttonListener(){
        binding.cvHutang.setOnClickListener{
            val intent = Intent(requireActivity(), BuyerHutangActivity::class.java)
            startActivity(intent)
        }

        binding.cvLunas.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerLunasActivity::class.java)
            startActivity(intent)
        }

        binding.cvAgreement.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerAgreementActivity::class.java)
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
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == true) {
                        debtList.add(debt)
                        debtIdMap[debt] = debtSnapshot.key ?: ""
                    }
                }

                if (debtList.isNotEmpty()) {
                    binding.rvHutang.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.rvHutang.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = ShopBuyerAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: ShopBuyerAdapter.OnItemClickCallback{
                    override fun onItemClicked(debt: DebtsModel) {
                        val debtId = debtIdMap[debt]
                        val intent = Intent(activity, DetailHutangActivity::class.java)
                        intent.putExtra(DetailHutangActivity.EXTRA_ID_DEBTS_HUTANG, debtId)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(requireContext(), "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getCountDataHutang(){
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(currentUser)
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
                binding.tvCountHutang.text = countDebts.toString()
                Log.d("TAG", "Jumlah hutang untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(requireContext(), "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getCountTotalHutang(){
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(currentUser)
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
                binding.tvTotalNominalHutangSaya.text = totalDebtAmount.toString()
                Log.d("TAG", "Total hutang untuk user $currentUser adalah: $totalDebtAmount")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(requireContext(), "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getCountDataHutangByStatus() {
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(currentUser)
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
                Log.d("TAG", "Jumlah hutang dengan status Lunas untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(requireContext(), "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getCountDataHutangByUserAgree(){
        val currentUser = auth.currentUser?.uid
        val query = dbRef.child("debts").orderByChild("debtorId").equalTo(currentUser)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.userAgree == false) {
                        debtList.add(debt)
                    }
                }

                // Jumlah data untuk status tertentu
                val countDebtsAgrement = debtList.size
                binding.tvCountHutangAgreement.text = countDebtsAgrement.toString()
                Log.d("TAG", "Jumlah hutang menunggu persetujuan user $currentUser adalah: $countDebtsAgrement")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(requireContext(), "Gagal memuat data hutang", Toast.LENGTH_SHORT).show()

            }
        })
    }

}