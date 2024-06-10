package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentDashboardSellerBinding

class DashboardSellerFragment : Fragment() {

    private lateinit var binding: FragmentDashboardSellerBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavHost()
    }

    private fun initNavHost(){
        navController = NavHostFragment.findNavController(this)
    }
}