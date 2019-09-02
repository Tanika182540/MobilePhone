package com.codemobiles.mymobilephone.presenter

import android.content.Context
import com.codemobiles.mobilephone.models.MobileBean

interface MobileListInterface {
    interface MobileListView{
        fun hideLoading()
    }

    interface MobileListPresenter{
        fun recieveBroadcast()
        fun recieveUpdateBroadcast()
//        fun removeFavorite(item: MobileBean, position: Int)
//        fun sendBroadcastMessage(content: ArrayList<MobileBean>)
//        fun addToFavorite(item: MobileBean, position: Int)
    }
}