package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.ui.mobilelist.CustomAdapter

interface MobileListInterface {
    interface MobileListView{
        fun hideLoading()
    }

    interface MobileListPresenter{
        fun feedData(mAdapter: CustomAdapter)
        fun recieveBroadcast()
//        fun recieveUpdateBroadcast()
        fun removeItemFavorite(item: MobileBean)
//        fun sendBroadcastMessage(content: ArrayList<MobileBean>)
        fun addItemToFavorite(item: MobileBean)
        fun navigateDetailPage(item: MobileBean)
        fun selectedSortItem(
            selectedItem: String,
            mDataArray: ArrayList<MobileBean>,
            mAdapter: CustomAdapter
        )
    }
}