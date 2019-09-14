package com.codemobiles.mymobilephone.database

import androidx.room.*
import com.codemobiles.mobilephone.models.MobileBean

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
    fun insertMobileList(mobileEntity: MobileEntity)


    @Query("DELETE FROM mobilePhone")
    fun clearMobileList()

    @Query( "select * from mobilePhone")
    fun querySort(): List<MobileEntity>

}