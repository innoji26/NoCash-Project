package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddTransaction.setOnClickListener {
            // Handle when button add transaction clicked
        }
    }


}