package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TransactionModel(
    val debtId: String,
    val shopId: String,
    val type: String,
    val amount: Int,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val imageTransaction: String
)
