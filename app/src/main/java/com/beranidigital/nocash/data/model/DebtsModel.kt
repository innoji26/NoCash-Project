package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DebtsModel(
    val debtorId: String = "",
    val creditorId: String = "",
    val amount: String = "0",
    val description: String = "",
    val dueDate: String = "",
    var status: String? = "Belum Lunas",
    var totalPaid: String? = "0", // Jumlah total yang sudah dibayar
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    val userAgree: Boolean? = false
){

}
