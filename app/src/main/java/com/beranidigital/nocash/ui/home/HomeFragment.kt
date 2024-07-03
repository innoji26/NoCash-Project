package com.beranidigital.nocash.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.FragmentHomeBinding
import com.beranidigital.nocash.ui.home.promo.RecycleViewAdapterPromo
import com.beranidigital.nocash.ui.login.LoginActivity
import com.beranidigital.nocash.ui.main_navigation.MainHomeActivity
import com.beranidigital.nocash.ui.profile.ProfileActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var promoRecyclerView: RecyclerView
    private lateinit var profileButton: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileButton = binding.ivProfile
        viewPager = binding.viewPagerHutanPiutang
        tabLayout = binding.tabsLayout

        viewPager.adapter = HomeTabMenuAdapter(requireActivity())

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        db = Firebase.database
        dbRef = FirebaseDatabase.getInstance().reference


        if(firebaseUser == null){
            //not signed in, launch the login activity
            val newIntent = Intent(requireContext(), LoginActivity::class.java)
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(newIntent)
            return
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hutang"
                else -> "Piutang"
            }
        }.attach()

        profileNavigation()
        setupPromo()
        setUserData()
        getCountDataHutang()
        getCountDataPiutang()
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
                binding.countPiutang.text = countDebts.toString()
                Log.d("TAG", "Jumlah Piutang untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
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
                binding.countHutang.text = countDebts.toString()
                Log.d("TAG", "Jumlah hutang untuk user $currentUser adalah: $countDebts")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
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
                    Log.w(TAG, "Data tidak ditemukan")
                    Toast.makeText(requireContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.w(TAG, "HomeFragment:failure", e)
                updateUI(null)
                Toast.makeText(requireContext(), "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun profileNavigation(){
        profileButton.setOnClickListener {
            val newIntent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            startActivity(Intent(requireContext(), MainHomeActivity::class.java))
        }
    }

    private fun setupPromo(){
        promoRecyclerView = binding.rvPromo
        promoRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val dummyPromo = listOf("Promo 1", "Promo 2", "Promo 3", "Promo 4", "Promo 5")
        promoRecyclerView.adapter = RecycleViewAdapterPromo(dummyPromo)
    }

    companion object{
        private const val TAG = "HomeFragment"
    }
}