package com.beranidigital.nocash.ui.home.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentHutangMenuBinding
import com.beranidigital.nocash.models.HutangModel

class HutangMenuFragment : Fragment() {
    private lateinit var binding: FragmentHutangMenuBinding
    private lateinit var recycleView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHutangMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleView = binding.recyclerViewHutang
        recycleView.layoutManager = LinearLayoutManager(requireContext())

        val data = mutableListOf<HutangModel>()

        for (i in 1..5){
            data.add(
                HutangModel(
                    1,
                    "Wahyu Budiman",
                    "Membeli gehu beli 5 gratis 1",
                    10,
                    10000,
                    "2021-10-10",
                    "Belum Lunas",
                    R.drawable.ic_profile_border
                ),
            )
        }

        recycleView.adapter = HutangRecyclerViewAdapter(data)

    }

}