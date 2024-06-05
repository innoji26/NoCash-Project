package com.beranidigital.nocash.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.beranidigital.nocash.databinding.FragmentHomeBinding
import com.beranidigital.nocash.ui.home.promo.RecycleViewAdapterPromo
import com.beranidigital.nocash.ui.profile.ProfileActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var promoRecyclerView: RecyclerView

    private lateinit var profileButton: ImageView

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


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hutang"
                else -> "Piutang"
            }
        }.attach()

        profileNavigation()

        setupPromo()
    }

    private fun profileNavigation(){
        profileButton.setOnClickListener {
            val newIntent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun setupPromo(){
        promoRecyclerView = binding.rvPromo
        promoRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val dummyPromo = listOf("Promo 1", "Promo 2", "Promo 3", "Promo 4", "Promo 5")
        promoRecyclerView.adapter = RecycleViewAdapterPromo(dummyPromo)
    }
}