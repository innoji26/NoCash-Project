package com.beranidigital.nocash.ui.profile.informations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.databinding.ItemTextMoreBinding

class PrivacyPolicyDescriptionRecycleViewAdapter(private val description: List<String>) :
    RecyclerView.Adapter<PrivacyPolicyDescriptionRecycleViewAdapter.ViewHolder>() {
        private lateinit var binding: ItemTextMoreBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemTextMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = description[position]
        holder.desctription.text = "- ${item}"
    }

    override fun getItemCount(): Int = description.size

    class ViewHolder(binding: ItemTextMoreBinding): RecyclerView.ViewHolder(binding.root){
        val desctription : TextView = binding.textItem
    }
}