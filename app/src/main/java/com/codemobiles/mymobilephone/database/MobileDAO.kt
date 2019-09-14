package com.codemobiles.mymobilephone.database

import androidx.room.*

@Dao
interface MobileDAO {

    @Query( "select * from mobilePhone")
    fun queryMobile(): MobileEntity

    @Insert
    fun insertMobileList(mobileEntity: MobileEntity)


    @Query("DELETE FROM mobilePhone")
    fun clearMobileList()

    @Query( "select * from mobilePhone")
    fun querySort(): List<MobileEntity>

}