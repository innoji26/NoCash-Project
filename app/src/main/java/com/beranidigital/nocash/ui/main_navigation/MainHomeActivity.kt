package com.beranidigital.nocash.ui.main_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ActivityMainHomeBinding
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

class MainHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainHomeBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigation: CurvedBottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigation = binding.bottomNavigation

        initNavHost()
        setUpBottomNavigation()

    }

    private fun initNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment

        navController = navHostFragment.navController
    }

    private fun MainHomeActivity.setUpBottomNavigation() {
        val bottomNavigationItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_seller_title,
                R.drawable.avd_seller,
                R.id.sellerFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_home_title,
                R.drawable.avd_home,
                R.id.homeFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_buyer_title,
                R.drawable.avd_buyer,
                R.id.buyerFragment,
            )
        )

        bottomNavigation.setMenuItems(bottomNavigationItems, 1)
        bottomNavigation.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_home).navigateUp()
                || super.onSupportNavigateUp()
    }
}