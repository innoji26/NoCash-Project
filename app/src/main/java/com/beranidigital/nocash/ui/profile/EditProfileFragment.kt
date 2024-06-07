package com.beranidigital.nocash.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentEditProfileBinding
import java.util.Calendar


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        spinner = binding.spGender
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.gender)
        )

        spinner.adapter = adapter

        datePickerDialogSetup()

        binding.btnSubmit.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun datePickerDialogSetup() {
        val editTextBirthday = binding.layoutBirthday.editText
        editTextBirthday?.showSoftInputOnFocus = false
        val cal = Calendar.getInstance()
        editTextBirthday?.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                editTextBirthday.setText("$dayOfMonth-${month + 1}-$year")
            }

            DatePickerDialog(
                requireContext(),
                datePicker,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }

}