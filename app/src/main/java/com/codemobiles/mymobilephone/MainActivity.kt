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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.codemobiles.mobilephone.MobileListFragment.Companion.mAdapter
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.presenter.MainActivityInterface
import com.codemobiles.mymobilephone.presenter.MainActivityPresenter
import com.codemobiles.mymobilephone.presenter.MainActivityPresenter.Companion.sortedArrayList
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter


class MainActivity : AppCompatActivity() ,MainActivityInterface.MainActivityView{

    companion object{

        var mRecieveArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
        var thisFavList: ArrayList<MobileBean> = ArrayList<MobileBean>()

    }
    private var selectedItem:String = "default"
    private lateinit var mMainActivityPresenter : MainActivityInterface.MainActivityPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onPageSelected(position: Int) {
//
//            }
//
//        })


        mMainActivityPresenter = MainActivityPresenter(this,this@MainActivity)
//        mMainActivityPresenter.feedData()
//        mMainActivityPresenter.recieveData()
//        mMainActivityPresenter.selectedSortItem(selectedItem, sortedArrayList)
//        mMainActivityPresenter.sendBroadcastMessage(mRecieveArray)

        sortButton.setOnClickListener {
            mMainActivityPresenter.showSortDialog()
        }

            //mMainActivityPresenter.recieveFavoriteData(selectedItem)


            //Log.d("favFromMain",favList.toString())
    }



//    private fun sendBroadcastFavMessage(thisFavList: ArrayList<MobileBean>) {
//        Intent(RECEIVED_FAVLIST).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, thisFavList)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(it)
//            //Log.d("sortList",thisFavList.toString())
//        }
//    }




//    fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
//        Intent(RECEIVED_TOKEN).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(it)
//            //Log.d("sortList",content.toString())
//        }
//
//    }


}
