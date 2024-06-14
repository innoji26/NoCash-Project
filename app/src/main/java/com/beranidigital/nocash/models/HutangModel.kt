package com.beranidigital.nocash.models

import com.beranidigital.nocash.R

data class HutangModel(
    val id: Int,
    val name: String,
    val description: String,
    val percentase: Int,
    val amount: Int? = 0,
    val date: String,
    val status: String,
    val image: Int? = R.drawable.ic_market,
) {
    override fun toString(): String {
        return "HutangModel(name='$name', description='$description', percentase=$percentase, amount=$amount, date='$date', status='$status', id=$id)"
    }
}
