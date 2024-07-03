package com.beranidigital.nocash.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentCheckShopBinding


class CheckShopFragment : Fragment() {
    private lateinit var binding: FragmentCheckShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateToko.setOnClickListener {
            findNavController().navigate(R.id.addTokoFragment)
        }
    }

}