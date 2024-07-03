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
import com.beranidigital.nocash.databinding.FragmentHutangMenuBinding
import com.beranidigital.nocash.ui.hutangPiutang.detail.DetailHutangActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HutangMenuFragment : Fragment() {
    private lateinit var binding: FragmentHutangMenuBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HutangRecyclerViewAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHutangMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewHutang
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth

        fetchHutangData()
    }

    private fun fetchHutangData() {
        val userId = auth.currentUser?.uid
//        var debtId: String? = ""

        // Query untuk mencari hutang berdasarkan debtorId
        val query = database.child("debts").orderByChild("debtorId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val debtList = mutableListOf<DebtsModel>()
                val debtIdMap = mutableMapOf<DebtsModel, String>()
                for (debtSnapshot in snapshot.children) {
                    val debt = debtSnapshot.getValue(DebtsModel::class.java)
                    if (debt != null && debt.status == "Belum Lunas" && debt.userAgree == true) {
                        debtIdMap[debt] = debtSnapshot.key ?: ""
                        debtList.add(debt)
                    }
                }

                if (debtList.isNotEmpty()) {
                    binding.recyclerViewHutang.visibility = View.VISIBLE
                    binding.IvNoData.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                } else {
                    binding.recyclerViewHutang.visibility = View.GONE
                    binding.IvNoData.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.VISIBLE
                }

                adapter = HutangRecyclerViewAdapter(debtList)
                recyclerView.adapter = adapter

                adapter.setOnItemClickCallback(object: HutangRecyclerViewAdapter.OnItemClickCallback{
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
            }
        })
    }
}