package com.codemobiles.mymobilephone.mobiledetailactivity

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
    private var mImageUrl:ArrayList<String> = arrayListOf()
    private lateinit var imageSlider: ImageSlider
    override fun feedImage(id: Int){
        val call = ApiInterface.getClient().getMobileImage(id)

        call.enqueue(object : Callback<List<MobileImage>> {
            override fun onFailure(call: Call<List<MobileImage>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<MobileImage>>, response: Response<List<MobileImage>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)


                    for (i in 0 until mDataArray.size) {
                        var url = mDataArray[i].url
                        if (url.contains("http")) {}
                        else url = "https://$url"
                        mImageUrl.add(i, url)
                    }

                    _view.setImage(mImageUrl)

                }

            }

        })

    }



}