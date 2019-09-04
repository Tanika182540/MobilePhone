package com.codemobiles.mymobilephone.database

import androidx.room.*

@Dao
interface MobileDAO {

    @Query( "select * from mobilePhone")
    fun queryMobile(): MobileEntity


    @Insert
    fun addFavorite(mobileEntity: MobileEntity)

//    @Insert
//    fun insertMobileList(mobileEntity:MobileEntity)
//
//    @Update
//    fun updataFavorite(mobileEntity: MobileEntity)
//
//    @Delete
//    fun deleteFavorite(mobileEntity: MobileEntity)
}