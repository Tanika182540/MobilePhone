package com.codemobiles.mymobilephone.favoritefragment

import com.codemobiles.mymobilephone.models.MobileBean
import com.codemobiles.mymobilephone.database.FavoriteEntity

interface FavListInterface {

    interface FavListView {
        fun hideLoading()
        fun getFav(selectedList: List<FavoriteEntity>?)
    }

    interface FavListPresenter {

        fun feedData(selectedItem: String)
        fun sortData(selectedItem: String)
        fun setUpWorkerThread()
        fun setupDatabase()
        fun deleteFavorite(id: Int)
        fun gotoDetailPage(item: MobileBean)
    }
}