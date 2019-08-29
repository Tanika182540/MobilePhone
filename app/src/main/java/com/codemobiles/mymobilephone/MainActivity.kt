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
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity() {

    private var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var sortedFavList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    val favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var thisFavList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    //private lateinit var mAdapter: MobileListFragment.CustomAdapter
    private var selectedItem:String = "default"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        feedData(selectedItem)

        sortButton.setOnClickListener {
            showSortDialog()
        }

        favoriteData(selectedItem)
    }

    fun favoriteData(selectedItem: String) {

        this.selectedItem = selectedItem

        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {

                    favList.clear()
                    favList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    thisFavList.clear()
                    thisFavList.addAll(favList)
                    //Log.d("MainFav",thisFavList.toString())

                }
            },
            IntentFilter(RECEIVED_NEW_MESSAGE)
        )
    }

    private fun sendBroadcastFavMessage(thisFavList: ArrayList<MobileBean>) {
        Intent(RECEIVED_FAVLIST).let {
            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, thisFavList)
            LocalBroadcastManager.getInstance(this).sendBroadcast(it)
            //Log.d("sortList",thisFavList.toString())
        }
    }

    fun showSortDialog() {
        lateinit var dialog:AlertDialog
        // Initialize a new instance of
        val array = arrayOf("Price low to high","Price high to low","Rating 5-1")

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        builder.setSingleChoiceItems(array,-1) { _, which->
            // Get the dialog selected item
            val selectedItem = array[which]

            try {
                feedData(selectedItem)
                feedData(selectedItem)
                dialog.dismiss()

            }catch (e:IllegalArgumentException){
                // Catch the color string parse exception

            }
            // Dismiss the dialog
            dialog.dismiss()
        }


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    fun feedData(selectedItem: String) {

        this.selectedItem = selectedItem

        val call = ApiInterface.getClient().getMobileDetail()

        //Check Request
        //Log.d("SCB_NETWORK " , call.request().url().toString())

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileBean>> {
            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
                //Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)
                    //Log.d("SCB_NETWORK",mDataArray.toString())

                    sortedList.clear()

                    when (selectedItem) {
                        "Rating 5-1" ->{

                            sortedList.addAll(mDataArray.sortedWith(compareBy({ it.rating })))
                            sendBroadcastMessage(sortedList)

                        }

                        "Price low to high" -> {

                            sortedList.addAll(mDataArray.sortedWith(compareBy({ it.price })))
                            sendBroadcastMessage(sortedList)
                        }
                        "Price high to low" ->{

                            sortedList.addAll( mDataArray.sortedByDescending { it.price })
                            sendBroadcastMessage(sortedList)
                        }
                        else -> { // Note the block

                            sortedList.addAll(mDataArray)
                            sendBroadcastMessage(sortedList)
                        }
                    }

                    //mAdapter.notifyDataSetChanged()
                }

            }


        })
    }

    fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
        Intent(RECEIVED_TOKEN).let {
            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
            LocalBroadcastManager.getInstance(this).sendBroadcast(it)
            //Log.d("sortList",content.toString())
        }

    }


}
