package com.beranidigital.nocash.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beranidigital.nocash.ui.home.menu.HutangMenuFragment
import com.beranidigital.nocash.ui.home.menu.PiutangMenuFragment

class HomeTabMenuAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HutangMenuFragment()
            else -> PiutangMenuFragment()
        }
    }

}