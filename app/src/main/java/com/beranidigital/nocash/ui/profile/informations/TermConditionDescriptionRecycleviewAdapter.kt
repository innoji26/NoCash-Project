package com.beranidigital.nocash.ui.profile.informations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.databinding.ItemTextMoreBinding

class TermConditionDescriptionRecycleviewAdapter(private val descriptions: List<String>) :
    RecyclerView.Adapter<TermConditionDescriptionRecycleviewAdapter.ViewHolder>() {

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
        holder.description.text = "- ${descriptions[position]}"
    }

    override fun getItemCount(): Int = descriptions.size

    class ViewHolder(binding: ItemTextMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        val description = binding.textItem
    }
}