package com.codemobiles.mymobilephone.mobiledetailactivity


interface MobileDetailInterface {
    interface DetailView {
        fun setImage(mDataArray: ArrayList<String>)
    }

    interface MobileDetailPresenter {
        fun feedImage(id: Int)
    }
}
