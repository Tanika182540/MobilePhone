package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean

interface FavListInterface {

    interface FavListView {
        fun hideLoading()
    }

    interface FavListPresenter {
        fun updateFavList(
            mDataArrayUpdate: ArrayList<MobileBean>,
            item: MobileBean,
            holder: MobileListFragment.CustomHolder
        )
        fun feedData(selectedItem: String)
        fun sortData(selectedItem: String)



    }
}