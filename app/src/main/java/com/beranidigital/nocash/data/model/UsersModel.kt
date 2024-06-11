package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UsersModel (
    val name: String? = null,
    val nik: String? = null,
    val gender: String? = null,
    val ttl: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val imageKtp: String? = null,
    val imageProfile: String? = null
){

}