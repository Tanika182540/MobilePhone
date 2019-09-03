package com.codemobiles.mymobilephone.presenter

import android.content.Context
import com.codemobiles.mobilephone.models.MobileBean

interface MainActivityInterface {

    interface MainActivityView{
       // fun showSortDialog(applicationContext: Context)
    }

    interface MainActivityPresenter{
        fun showSortDialog()
//        fun feedData()
        fun selectedSortItem(selectedItem: String,mDataArray: ArrayList<MobileBean>)
        fun sendBroadcastMessage(content: ArrayList<MobileBean>)
        fun recieveData()
//        fun recieveFavoriteData(selectedItem: String)
    }
}