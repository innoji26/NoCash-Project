package com.beranidigital.nocash.ui.home.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.databinding.FragmentPiutangMenuBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailPiutangActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PiutangMenuFragment : Fragment() {

    private lateinit var binding : FragmentPiutangMenuBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PiutangRecyclerViewAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPiutangMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewPiutang
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth

        fetchPiutangData()
    }

    private fun fetchPiutangData() {
        val userId = auth.currentUser?.uid

        // Query untuk mencari Piutang berdasarkan debtorId
        val query = database.child("debts").orderByChild("creditorId").equalTo(userId)
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
                    binding.recyclerViewPiutang.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.recyclerViewPiutang.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = PiutangRecyclerViewAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: PiutangRecyclerViewAdapter.OnItemClickCallback{
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
}