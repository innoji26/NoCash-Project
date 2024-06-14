package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DebtsModel(
    val debtorId: Int,
    val shopeerId: Int,
    val amount: Int,
    val description: String,
    val dueDate: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val imageDebt: String? = null,
)
