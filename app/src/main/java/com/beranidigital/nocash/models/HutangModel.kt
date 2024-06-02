package com.beranidigital.nocash.models

data class HutangModel(
    val id: Int,
    val name: String,
    val description: String,
    val percentase: Int,
    val amount: Int,
    val date: String,
    val status: String
) {
    override fun toString(): String {
        return "HutangModel(name='$name', description='$description', percentase=$percentase, amount=$amount, date='$date', status='$status', id=$id)"
    }
}
