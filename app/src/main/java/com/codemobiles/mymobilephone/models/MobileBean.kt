package com.codemobiles.mobilephone.models

import com.codemobiles.cmscb.models.Youtube

data class MobileBean(
    val brand: String,
    val description: String,
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val thumbImageURL: String
)