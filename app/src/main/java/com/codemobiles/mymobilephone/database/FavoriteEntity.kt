package com.codemobiles.mymobilephone.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritePhone")
data class FavoriteEntity(
    @PrimaryKey val mobileID: Int,
    val description: String,
    val thumbImageURL: String,
    val name: String,
    val price: Double,
    val brand: String,
    val rating: Double
)