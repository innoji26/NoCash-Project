package com.beranidigital.nocash.ui.shop.buyer.aggrement

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ItemHutangAgreementBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BuyerAgreementAdapter(private val debts: List<DebtsModel>
): RecyclerView.Adapter<BuyerAgreementAdapter.ViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var binding: ItemHutangAgreementBinding
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyerAgreementAdapter.ViewHolder {
        binding = ItemHutangAgreementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyerAgreementAdapter.ViewHolder, position: Int) {
        val item = debts[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = debts.size

    inner class ViewHolder(binding: ItemHutangAgreementBinding) : RecyclerView.ViewHolder(binding.root){
        val name: TextView = binding.titleItem
        val description: TextView = binding.descriptionItem
        val amount : TextView = binding.amountItem
        val image: ImageView = binding.iconItem


        fun bind(debt: DebtsModel) {
            description.text = debt.description
            amount.text = "Rp. ${debt.amount}"
            Glide.with(itemView.context)
                .load(R.drawable.shop_icon)
                .circleCrop()
                .into(binding.iconItem)
            fetchUserName(debt.creditorId) { debtorName ->
                name.text = debtorName
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(debts[adapterPosition])
            }

            binding.btnTolak.setOnClickListener {
                onItemClickCallback.onRejectClicked(debt)
            }

            binding.btnSetuju.setOnClickListener {
                onItemClickCallback.onApproveClicked(debt)
            }

        }

        private fun fetchUserName(userId: String, callback: (String) -> Unit) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UsersModel::class.java)
                    callback(user?.name ?: "Unknown User")
                }

                override fun onCancelled(error: DatabaseError) {
                    callback("Unknown User")
                }
            })
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(debt: DebtsModel)
        fun onApproveClicked(debt: DebtsModel)
        fun onRejectClicked(debt: DebtsModel)
    }
}