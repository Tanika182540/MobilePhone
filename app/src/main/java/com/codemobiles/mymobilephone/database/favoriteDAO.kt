package com.codemobiles.mymobilephone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface favoriteDAO {

    @Query( "select * from favoritePhone")
    fun queryFavMobile(): List<FavoriteEntity>

    @Query( "select * from favoritePhone where mobileID = :mobileId")
    fun queryDuplicateId(mobileId: Int): FavoriteEntity

    @Insert
    fun addFavorite(favoriteEntity: FavoriteEntity)

    @Update
    fun updataFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favoritePhone WHERE mobileID = :mobileId")
    fun deleteFavorite(mobileId:Int)

    @Query( "select * from favoritePhone ORDER BY price DESC")
    fun querySortPriceH(): List<FavoriteEntity>

    @Query( "select * from favoritePhone ORDER BY price")
    fun querySortPriceL(): List<FavoriteEntity>

    @Query( "select * from favoritePhone ORDER BY rating DESC")
    fun querySortRating(): List<FavoriteEntity>
}