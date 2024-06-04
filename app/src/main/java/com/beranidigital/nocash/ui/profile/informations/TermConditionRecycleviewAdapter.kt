package com.beranidigital.nocash.ui.profile.informations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.databinding.ItemTextBinding
import com.beranidigital.nocash.models.TextModel

class TermConditionRecycleviewAdapter(private val data: List<TextModel>) :
    RecyclerView.Adapter<TermConditionRecycleviewAdapter.ViewHolder>() {

    private lateinit var binding: ItemTextBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       binding = ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = data[position]
        holder.title.text = "${position+1}. ${item.title}"

        holder.recycleView.layoutManager = LinearLayoutManager(holder.recycleView.context)
        holder.recycleView.adapter = TermConditionDescriptionRecycleviewAdapter(item.description)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {
        val title : TextView = binding.titleItem
        val recycleView: RecyclerView = binding.recyclerViewTermCondition
    }
}