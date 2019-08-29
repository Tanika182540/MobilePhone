package com.codemobiles.mobilephone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.RECEIVED_PLAYERID
import com.codemobiles.mymobilephone.RECEIVED_TOKEN
import com.codemobiles.mymobilephone.models.MobileImage
import com.ouattararomuald.slider.ImageSlider
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.picasso.PicassoImageLoaderFactory
import kotlinx.android.synthetic.main.activity_mobile_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileDetailActivity : AppCompatActivity() {

    private var mDataArray: ArrayList<MobileImage> = ArrayList<MobileImage>()

    private lateinit var imageSlider: ImageSlider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_detail)

        val name = intent.getStringExtra("name")
        val brand = intent.getStringExtra("brand")
        val description = intent.getStringExtra("description")
        val id = intent.getIntExtra("id", 0)
        val price = intent.getDoubleExtra("price",0.0)
        val rating = intent.getDoubleExtra("rating",0.0)
        modeText.text = name
        brandText.text = brand
        detailText.text = description
        textViewRating.text = "rating : " + rating.toString()
        textViewPrice.text = "price : " + price.toString()

        feedImage(id)

    }



    fun feedImage(id: Int) {
            val call = ApiInterface.getClient().getMobileImage(id)

            //Check Request
            Log.d("SCB_NETWORK " , call.request().url().toString())

            //change <YoutubeResponse>
            call.enqueue(object : Callback<List<MobileImage>> {
                override fun onFailure(call: Call<List<MobileImage>>, t: Throwable) {
                    Log.d("SCB_NETWORK " , t.message.toString())
                }

                override fun onResponse(call: Call<List<MobileImage>>, response: Response<List<MobileImage>>) {
                    if(response.isSuccessful){
                        mDataArray.clear()
                        mDataArray.addAll(response.body()!!)
                        Log.d("SCB_NETWORK",mDataArray.toString())


                        var imageList = arrayListOf(
                        "http://i.imgur.com/CqmBjo5.jpg",
                        "http://i.imgur.com/zkaAooq.jpg",
                        "http://i.imgur.com/0gqnEaY.jpg"
                        )
                        imageList.clear()
                        var size = mDataArray.size

                        for (i in 0 until  size){

                            if(mDataArray[i].url.contains("http")){
                                imageList.add(mDataArray[i].url)
                            }else{
                                imageList.add("http://" + mDataArray[i].url)
                            }


                        }
                        Log.d("SCB_NETWORK",imageList.toString())

                        var imageUrls = arrayListOf(imageList)
                        imageSlider = findViewById(R.id.image_slider)
                        imageSlider.adapter = SliderAdapter(
                            applicationContext,
                            PicassoImageLoaderFactory(),
                            imageUrls = imageList
                        )
                    }

                }

            })
    }


}
