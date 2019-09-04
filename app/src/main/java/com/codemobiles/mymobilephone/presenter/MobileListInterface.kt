package com.codemobiles.mymobilephone.presenter

import android.view.View
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean

interface MobileListInterface {
    interface MobileListView {

    }

    interface MobileListPresenter {
        fun feedData(selectedItem: String)
        fun removeFavorite(item: MobileBean, position: Int)
        fun addToFavorite(item: MobileBean, position: Int)
//        fun recieveBroadcast(view:View)
        fun gotoDetailPage(item: MobileBean)
        fun setUpWorkerThread()
        fun setupDatabase()
    }
}