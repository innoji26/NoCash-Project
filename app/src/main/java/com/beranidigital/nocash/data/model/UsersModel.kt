package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UsersModel (
    val name: String,
    val nik: String,
    val gender: String,
    val ttl: String,
    val address: String,
    val phone: String,
    val email: String,
    val imageKtp: String,
    val imageProfile: String? = null
){

}