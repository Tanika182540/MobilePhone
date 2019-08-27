package com.codemobiles.mobilephone

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.codemobiles.mymobilephone.R
import kotlinx.android.synthetic.main.activity_mobile_detail.*

class MobileDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_detail)

        val name = intent.getStringExtra("name")
        val brand = intent.getStringExtra("brand")
        val description = intent.getStringExtra("description")
        val image = intent.getStringExtra("image")
        modeText.text = name
        brandText.text = brand
        detailText.text = description
        Glide.with(this).load(image).into(imageView)

    }

}
