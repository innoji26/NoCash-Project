package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentPlaceholderSellerBinding

class PlaceholderSellerFragment : Fragment() {

    private lateinit var binding: FragmentPlaceholderSellerBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceholderSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavhost()

        binding.registerSellerButton.setOnClickListener {
            navController.navigate(R.id.action_placeholderSellerFragment_to_registerSellerFragment)
        }
    }

    private fun initNavhost(){
        navController = NavHostFragment.findNavController(this)
    }

}