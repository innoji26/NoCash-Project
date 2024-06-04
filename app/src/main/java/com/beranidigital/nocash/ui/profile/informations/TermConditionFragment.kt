package com.beranidigital.nocash.ui.profile.informations

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.constants.TermCondition
import com.beranidigital.nocash.databinding.FragmentProfileBinding
import com.beranidigital.nocash.databinding.FragmentTermConditionBinding

class TermConditionFragment : Fragment() {

    private lateinit var binding: FragmentTermConditionBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermConditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setupWithNavController(findNavController())

        recyclerView = binding.recyclerViewTermCondition
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = TermConditionRecycleviewAdapter(TermCondition().data)


    }

}