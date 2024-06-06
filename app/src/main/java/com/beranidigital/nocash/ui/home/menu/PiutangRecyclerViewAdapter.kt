package com.beranidigital.nocash.ui.home.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.databinding.ItemHutangBinding
import com.beranidigital.nocash.models.HutangModel


class PiutangRecyclerViewAdapter(
    private val values: List<HutangModel>
) : RecyclerView.Adapter<PiutangRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: ItemHutangBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHutangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.name
        holder.description.text = item.description
        holder.amount.text = "Rp. ${item.amount}"
        holder.image.setImageResource(item.image!!)
        holder.progressBar.progress = item.percentase
        holder.progressBarText.text = "${item.percentase}%"
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(binding: ItemHutangBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.titleItem
        val description: TextView = binding.descriptionItem
        val amount : TextView = binding.amountItem
        val image : ImageView = binding.iconItem
        val progressBar: ProgressBar = binding.progressItem
        val progressBarText: TextView = binding.progressTextItem

    }

}