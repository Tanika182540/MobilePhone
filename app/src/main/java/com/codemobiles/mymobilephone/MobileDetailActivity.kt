package com.codemobiles.mobilephone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.models.MobileImage
import com.codemobiles.mymobilephone.presenter.MobileDetailInterface
import com.codemobiles.mymobilephone.presenter.MobileDetailPresenter
import com.ouattararomuald.slider.ImageSlider
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.picasso.PicassoImageLoaderFactory
import kotlinx.android.synthetic.main.activity_mobile_detail.*

class MobileDetailActivity : AppCompatActivity(),MobileDetailInterface.DetailView{
    override fun setImage(imageArray: ArrayList<MobileImage>) {
        this.mDataArray = imageArray
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
        //Log.d("SCB_NETWORK",imageList.toString())

        var imageUrls = arrayListOf(imageList)
        imageSlider = findViewById(R.id.image_slider)
        imageSlider.adapter = SliderAdapter(
            applicationContext,
            PicassoImageLoaderFactory(),
            imageUrls = imageList
        )
    }

    private var mDataArray: ArrayList<MobileImage> = ArrayList<MobileImage>()
    lateinit var mMobileDetailPresenter: MobileDetailInterface.MobileDetailPresenter
    private lateinit var imageSlider: ImageSlider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_detail)

        mMobileDetailPresenter = MobileDetailPresenter(this,applicationContext)
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

        mMobileDetailPresenter.feedImage(id)

    }





}
