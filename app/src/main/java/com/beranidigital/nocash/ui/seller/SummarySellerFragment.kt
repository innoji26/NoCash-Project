package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentSummarySellerBinding

class SummarySellerFragment : Fragment() {

    private lateinit var binding: FragmentSummarySellerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummarySellerBinding.inflate(inflater, container, false)
        return binding.root
    }


}