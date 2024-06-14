package com.beranidigital.nocash.ui.home.promo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.databinding.ItemPromoBinding

class RecycleViewAdapterPromo(private val data: List<String>) :
    RecyclerView.Adapter<RecycleViewAdapterPromo.ViewHolder>() {

        private lateinit var binding: ItemPromoBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item
    }

    class ViewHolder(binding: ItemPromoBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.tvTitle
    }
}