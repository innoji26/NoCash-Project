package com.beranidigital.nocash.ui.main_navigation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityMainHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

class MainHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainHomeBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigation: CurvedBottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigation = binding.bottomNavigation

        auth = Firebase.auth
        db = Firebase.database

        initNavHost()
        setUpBottomNavigation()

    }

    private fun initNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment

        navController = navHostFragment.navController

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.sellerFragment) {
//                checkUserToko()
//            }
//        }
    }

    private fun MainHomeActivity.setUpBottomNavigation() {
        val bottomNavigationItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_buyer_title,
                R.drawable.avd_buyer,
                R.id.shopSellerFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_home_title,
                R.drawable.avd_home,
                R.id.homeFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_seller_title,
                R.drawable.avd_seller,
                R.id.shopBuyerFragment,
            )

        )

        bottomNavigation.setMenuItems(bottomNavigationItems, 1)
        bottomNavigation.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_home).navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun checkUserToko() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.getReference("shops").orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // User has a store
                            navController.navigate(R.id.shopSellerFragment)
                            Log.d("checkFragment", "toko ditemukan")
                        }else{
                            navController.navigate(R.id.checkTokoFragment)
                            Log.d("checkFragment", "toko tidak ditemukan")

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle possible errors.
                        Log.e("checkFragment", "Database error: ${error.message}")
                    }
                })
        }else{
            Toast.makeText(this@MainHomeActivity, "Please login first", Toast.LENGTH_SHORT).show()
        }
    }
}