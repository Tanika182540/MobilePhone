package com.codemobiles.mymobilephone.database

import androidx.room.*

@Dao
interface MobileDAO {

    @Query( "select * from mobilePhone")
    fun queryMobile(): MobileEntity



//    @Transaction
//    fun setMobileList(mobileEntity: MobileEntity){
//        clearMobileList()
//        insertMobileList(mobileEntity)
//    }

    @Insert
    fun insertMobileList(mobileEntity:MobileEntity)


//    @Query("DELETE FROM mobilePhone")
//    fun clearMobileList()
}