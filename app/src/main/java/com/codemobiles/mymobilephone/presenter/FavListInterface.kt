package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.database.FavoriteEntity

interface FavListInterface {

    interface FavListView {
        fun hideLoading()
        fun getFav(selectedList: List<FavoriteEntity>?)
    }

    interface FavListPresenter {
        fun updateFavList(
            mDataArrayUpdate: ArrayList<MobileBean>,
            item: MobileBean,
            holder: MobileListFragment.CustomHolder
        )
        fun feedData(selectedItem: String)
        fun sortData(selectedItem: String)
        fun setUpWorkerThread()
        fun setupDatabase()
        fun deleteFavorite(id: Int)

    }
}