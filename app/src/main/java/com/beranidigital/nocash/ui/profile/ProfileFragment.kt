package com.beranidigital.nocash.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavHost()

        binding.contactUs.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_contactUsFragment)
        }

        binding.termCondition.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_termConditionFragment)
        }

        binding.privacyPolicy.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_privacyPolicyFragment)
        }

        binding.aboutUs.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_aboutUsFragment)
        }
    }

    private fun initNavHost() {
        navController = NavHostFragment.findNavController(this)

    }
}