package com.codemobiles.mymobilephone.presenter

import com.codemobiles.mymobilephone.models.MobileImage


interface MobileDetailInterface {
        interface DetailView {
            fun setImage(mDataArray: ArrayList<String>)
        }

        interface MobileDetailPresenter {
            fun feedImage(id:Int)
        }
    }
