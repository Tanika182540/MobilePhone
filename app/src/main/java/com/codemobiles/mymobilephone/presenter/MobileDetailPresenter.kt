package com.codemobiles.mymobilephone.presenter

import android.content.Context
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.models.MobileImage
import com.ouattararomuald.slider.ImageSlider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileDetailPresenter(
    private var _view: MobileDetailInterface.DetailView,
    private var applicationContext: Context
) : MobileDetailInterface.MobileDetailPresenter {

    private var mDataArray: ArrayList<MobileImage> = ArrayList<MobileImage>()
    private lateinit var imageSlider: ImageSlider
    override fun feedImage(id: Int){
        val call = ApiInterface.getClient().getMobileImage(id)

        //Check Request
        //Log.d("SCB_NETWORK " , call.request().url().toString())

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileImage>> {
            override fun onFailure(call: Call<List<MobileImage>>, t: Throwable) {
                //Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileImage>>, response: Response<List<MobileImage>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)
                    //Log.d("SCB_NETWORK",mDataArray.toString())

                    _view.setImage(mDataArray)

                }

            }

        })

    }



}