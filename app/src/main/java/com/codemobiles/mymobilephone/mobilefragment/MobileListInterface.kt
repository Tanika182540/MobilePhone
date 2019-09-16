package com.codemobiles.mymobilephone.mobilefragment

import com.codemobiles.mobilephone.models.MobileBean

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
        fun gotoDetailPage(item: MobileBean)
        fun setUpWorkerThread()
        fun setupDatabase()
        fun getFavoriteList()
        fun sendTask(task: Runnable)
        fun sortData(sort: String, moblieList: ArrayList<MobileBean>)
    }
}