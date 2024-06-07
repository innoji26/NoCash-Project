package com.beranidigital.nocash.ui.seller

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentRegisterSellerBinding

class RegisterSellerFragment : Fragment() {
    private lateinit var binding : FragmentRegisterSellerBinding
    private lateinit var navController: NavController
    private lateinit var categoryDropdown : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavhost()

        categoryDropdown = binding.spinnerCategory
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category)
        )
        categoryDropdown.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnRegisterSeller.setOnClickListener {
            // show dialog with 2 seconds delay
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_success_register_seller)
            dialog.show()
            Handler().postDelayed({
                dialog.dismiss()
                navController.navigate(R.id.action_registerSellerFragment_to_dashboardSeller)
            }, 3000)
        }

    }

    private fun initNavhost(){
        navController = NavHostFragment.findNavController(this)
    }


}