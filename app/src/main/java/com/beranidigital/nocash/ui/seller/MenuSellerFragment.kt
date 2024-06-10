package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentMenuSellerBinding

class MenuSellerFragment : Fragment() {
    private lateinit var binding: FragmentMenuSellerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuSellerBinding.inflate(inflater, container, false)
        return binding.root
    }


}