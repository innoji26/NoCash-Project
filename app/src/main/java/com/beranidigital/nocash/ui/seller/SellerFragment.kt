package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beranidigital.nocash.databinding.FragmentSellerBinding

class SellerFragment : Fragment() {
    private lateinit var binding: FragmentSellerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellerBinding.inflate(inflater, container, false)
        return binding.root
    }


}