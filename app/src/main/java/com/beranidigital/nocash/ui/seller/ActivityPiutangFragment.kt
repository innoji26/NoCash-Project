package com.beranidigital.nocash.ui.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.FragmentActivityPiutangBinding
import com.beranidigital.nocash.models.HutangModel
import com.beranidigital.nocash.ui.home.menu.PiutangRecyclerViewAdapter

class ActivityPiutangFragment : Fragment() {
    private lateinit var binding: FragmentActivityPiutangBinding
    private lateinit var recycleView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityPiutangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleView = binding.rvPiutang
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        val data  =  mutableListOf<HutangModel>()
        for (i in 1..5){
            data.add(
                HutangModel(
                    1,
                    "Wawan Budiman",
                    "Membeli gehu beli 5 gratis 1",
                    10 + (10 * i),
                    10000,
                    "2021-10-10",
                    "Belum Lunas",
                    R.drawable.ic_profile_border
                ),
            )
        }
        recycleView.adapter = PiutangRecyclerViewAdapter(data)

    }


}