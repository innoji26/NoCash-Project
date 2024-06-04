package com.beranidigital.nocash.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShopModel(
    val shopName: String,
    val shopType: String,
    val shopAddress: String,
    val userId: String
){

}
