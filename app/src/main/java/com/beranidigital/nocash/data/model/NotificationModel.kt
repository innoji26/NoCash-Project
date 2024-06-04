package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class NotificationModel (
    val userId: Int,
    val title: String,
    val body: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
){

}