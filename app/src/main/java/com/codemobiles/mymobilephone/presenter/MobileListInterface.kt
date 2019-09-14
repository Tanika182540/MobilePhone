package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.database.MobileEntity

interface MobileListInterface {
    interface MobileListView {

        fun favoriteListData(selectedList: List<MobileBean>?)
        fun hideLoading()
        fun showData()
        fun submitlist(mobilelist: ArrayList<MobileBean>)
        fun submitSortlist(mobilelist: ArrayList<MobileBean>)
    }

    interface MobileListPresenter {
        fun feedData(selectedItem: String): ArrayList<MobileBean>
        fun removeFavorite(item: MobileBean)
        fun addToFavorite(item: MobileBean)
        //        fun recieveBroadcast(view:View)
        fun gotoDetailPage(item: MobileBean)
        fun setUpWorkerThread()
        fun setupDatabase()
        fun getFavoriteList()
        fun sendTask(task:Runnable)
        fun sortData(sort:String,moblieList:ArrayList<MobileBean>)
    }
}