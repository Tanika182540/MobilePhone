package com.codemobiles.mymobilephone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.presenter.MainInterface
import com.codemobiles.mymobilephone.presenter.MainPresenter
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity(),MainInterface.MainView {

    companion object{
        var selectedItem:String = "default"
        lateinit var mMainPresenter : MainInterface.MainPresenter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

//        feedData(selectedItem)
        mMainPresenter = MainPresenter(this, this@MainActivity)

        sortButton.setOnClickListener {
            mMainPresenter.showSortDialog()
        }

//        favoriteData(selectedItem)
    }

//    fun favoriteData(selectedItem: String) {
//
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            object : BroadcastReceiver(){
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    favList.clear()
//                    favList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    thisFavList.clear()
//                    thisFavList.addAll(favList)
//                    //Log.d("MainFav",thisFavList.toString())
//
//                }
//            },
//            IntentFilter(RECEIVED_NEW_MESSAGE)
//        )
//    }




}
