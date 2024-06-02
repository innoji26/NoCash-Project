package com.beranidigital.nocash.ui.home.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.databinding.HutangItemBinding
import com.beranidigital.nocash.models.HutangModel


class PiutangRecyclerViewAdapter(
    private val values: List<HutangModel>
) : RecyclerView.Adapter<PiutangRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: HutangItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HutangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.name
        holder.description.text = item.description
        holder.amount.text = "Rp. ${item.amount}"
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(binding: HutangItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.titleItem
        val description: TextView = binding.descriptionItem
        val amount : TextView = binding.amountItem

    }

}