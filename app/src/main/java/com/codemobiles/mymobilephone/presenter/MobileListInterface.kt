package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.database.FavoriteEntity

interface MobileListInterface {
    interface MobileListView {

        fun checkFavoriteButton(selectedList: List<FavoriteEntity>?)
        fun hideLoading()
        fun showData()
        fun getListMobile(mMobileArray: ArrayList<MobileBean>)
    }

    interface MobileListPresenter {
        fun feedData(selectedItem: String): ArrayList<MobileBean>
        fun removeFavorite(item: MobileBean, position: Int)
        fun addToFavorite(item: MobileBean, position: Int)
//        fun recieveBroadcast(view:View)
        fun gotoDetailPage(item: MobileBean)
        fun setUpWorkerThread()
        fun setupDatabase()
        fun addFavoriteButton()
        fun loadDatabase(): ArrayList<MobileBean>
        fun sendTask(task:Runnable)

    }
}