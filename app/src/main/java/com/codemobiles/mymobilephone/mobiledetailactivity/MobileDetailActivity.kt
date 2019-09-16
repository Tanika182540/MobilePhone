package com.codemobiles.mymobilephone.mobiledetailactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_mobile_detail.*

class MobileDetailActivity : AppCompatActivity(),
    MobileDetailInterface.DetailView {

    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAapter: ViewPagerAdapter
    private var width: Int = 0
    private var height: Int = 0
    private lateinit var mMobileDetailPresenter: MobileDetailInterface.MobileDetailPresenter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_detail)

        val displayMetrics = DisplayMetrics()
        viewPager = findViewById(R.id.view_pager_image)
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = (displayMetrics.heightPixels * 35) / 100
        mMobileDetailPresenter = MobileDetailPresenter(this)

        val name = intent.getStringExtra("name")
        val brand = intent.getStringExtra("brand")
        val description = intent.getStringExtra("description")
        val id = intent.getIntExtra("id", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val rating = intent.getDoubleExtra("rating", 0.0)

        modeText.text = name
        brandText.text = brand
        detailText.text = description
        textViewRating.text = "rating : $rating"
        textViewPrice.text = "price : $price"

        mMobileDetailPresenter.feedImage(id)

    }

    override fun setImage(imageArray: ArrayList<String>) {

        viewPagerAapter = ViewPagerAdapter(
            this,
            imageArray,
            width,
            height
        )
        val params = LinearLayout.LayoutParams(width, height)
        viewPager.layoutParams = params
        viewPager.adapter = viewPagerAapter
        viewPager.adapter = viewPagerAapter

    }


}
