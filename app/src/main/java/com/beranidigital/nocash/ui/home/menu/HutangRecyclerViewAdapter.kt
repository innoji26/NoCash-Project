package com.beranidigital.nocash.ui.home.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beranidigital.nocash.R
import com.beranidigital.nocash.data.model.DebtsModel
import com.beranidigital.nocash.data.model.UsersModel
import com.beranidigital.nocash.databinding.ItemHutangBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class HutangRecyclerViewAdapter(
    private val debts: List<DebtsModel>
) : RecyclerView.Adapter<HutangRecyclerViewAdapter.ViewHolder>() {


    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var binding: ItemHutangBinding
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHutangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = debts[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = debts.size

    inner class ViewHolder(binding: ItemHutangBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.titleItem
        val description: TextView = binding.descriptionItem
        val amount : TextView = binding.amountItem
        val statusJatuhTempo = binding.statusItem

        val image: ImageView = binding.iconItem
        val progressBar:ProgressBar = binding.progressItem
        val progressBarText: TextView = binding.progressTextItem

        fun bind(debt: DebtsModel) {
            description.text = debt.description
            amount.text = "Rp. ${debt.amount}"
            Glide.with(itemView.context)
                .load(R.drawable.shop_icon)
                .circleCrop()
                .into(binding.iconItem)
            val hutang = debt.amount.toInt()
            val dibayar = debt.totalPaid?.toInt() ?: 0
            val presentase = if(dibayar ==0) 0 else ((dibayar.toDouble() / hutang.toDouble()) * 100).toInt()
            progressBar.progress = presentase
            progressBarText.text = "${presentase}%"
            fetchUserName(debt.creditorId) { debtorName ->
                name.text = debtorName
            }

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(debts[adapterPosition])
            }

            /// Calculate days until due date
            if (!debt.dueDate.isNullOrEmpty()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                try {
                    val dueDate = dateFormat.parse(debt.dueDate)
                    if (dueDate != null) {
                        val daysUntilDue = calculateDaysUntilDue(dueDate)
                        if (daysUntilDue > 0) {
                            statusJatuhTempo.text = "Jatuh tempo $daysUntilDue hari lagi"
                        } else if (daysUntilDue == 0) {
                            statusJatuhTempo.text = "Jatuh tempo hari ini"
                        } else {
                            statusJatuhTempo.text = "Lewat jatuh tempo ${-daysUntilDue} hari"
                        }
                    } else {
                        statusJatuhTempo.text = "Jatuh tempo tidak valid"
                    }
                } catch (e: ParseException) {
                    statusJatuhTempo.text = "Format tanggal tidak valid"
                }
            } else {
                statusJatuhTempo.text = "Jatuh tempo tidak ditentukan"
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

        private fun calculateDaysUntilDue(dueDate: Date): Int {
            val currentDate = Calendar.getInstance().time
            val diffInMillis = dueDate.time - currentDate.time
            return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(debt: DebtsModel)
    }

}