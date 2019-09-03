package com.codemobiles.mymobilephone.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.MobileListFragment
//import com.codemobiles.mobilephone.MobileListFragment.Companion.mAdapter
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.MainActivity.Companion.mRecieveArray
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE2
import com.codemobiles.mymobilephone.RECEIVED_TOKEN
import java.lang.IllegalArgumentException

class MainActivityPresenter(_view: MainActivityInterface.MainActivityView, context: Context) : MainActivityInterface.MainActivityPresenter {

    private var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var context: Context = context
    var view = _view
    var mMobileListFragment:MobileListFragment = MobileListFragment()
    private var selectedItem:String = "default"

    companion object{
        var sortedArrayList: ArrayList<MobileBean> = ArrayList<MobileBean>()
        var dataList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    }

//    override fun feedData() {
//        this.selectedItem = selectedItem
//
//        val call = ApiInterface.getClient().getMobileDetail()
//
//        //Check Request
//        //Log.d("SCB_NETWORK " , call.request().url().toString())
//
//        //change <YoutubeResponse>
//        call.enqueue(object : Callback<List<MobileBean>> {
//            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
//                //Log.d("SCB_NETWORK " , t.message.toString())
//            }
//
//            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
//                if(response.isSuccessful){
//                    mDataArray.clear()
//                    mDataArray.addAll(response.body()!!)
//                    Log.d("SCB_NETWORK",mDataArray.toString())
//
//                    sendBroadcastToMain(mDataArray)
//                    mAdapter.notifyDataSetChanged()
//
//                }
//            }
//        })
//    }

    private fun sendBroadcastToMain(mDataArray: ArrayList<MobileBean>) {
        Intent(RECEIVED_MESSAGE2).let {
            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, mDataArray)
            LocalBroadcastManager.getInstance(context).sendBroadcast(it)
            //Log.d("sortList",content.toString())
        }
    }

    override fun recieveData() {


        LocalBroadcastManager.getInstance(context).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {

                    mRecieveArray.clear()
                    mRecieveArray.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    sortedArrayList.addAll(mRecieveArray)
                    Log.d("ME", sortedArrayList.toString())

                }
            },
            IntentFilter(RECEIVED_MESSAGE2)
        )
    }

    override fun selectedSortItem(selectedItem: String,mMobileArray: ArrayList<MobileBean>) {

        this.selectedItem = selectedItem

        when (selectedItem) {
            "Rating 5-1" ->{

                dataList.clear()
                dataList.addAll(mRecieveArray.sortedWith(compareBy({ it.rating })))
                sortedArrayList.clear()
                sortedArrayList.addAll(dataList)
                sendBroadcastMessage(sortedArrayList)

            }

            "Price low to high" -> {

                dataList.clear()
                dataList.addAll(mRecieveArray.sortedWith(compareBy({ it.price })))
                sortedArrayList.clear()
                sortedArrayList.addAll(dataList)
                sendBroadcastMessage(sortedArrayList)
            }
            "Price high to low" ->{

                dataList.clear()
                dataList.addAll( mRecieveArray.sortedByDescending { it.price })
                sortedArrayList.clear()
                sortedArrayList.addAll(dataList)
                sendBroadcastMessage(sortedArrayList)
            }
            else -> { // Note the block

                dataList.clear()
                dataList.addAll(mRecieveArray)
                sortedArrayList.clear()
                sortedArrayList.addAll(dataList)
                sendBroadcastMessage(sortedArrayList)
                Log.d("MySort",sortedArrayList.toString())
            }
        }

    }

    override fun showSortDialog() {
        lateinit var dialog: AlertDialog
        // Initialize a new instance of
        val array = arrayOf("Price low to high", "Price high to low", "Rating 5-1")

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(context)

        builder.setSingleChoiceItems(array, -1) { _, which ->
            val selectedItem = array[which]

            try {

                selectedSortItem(selectedItem,mDataArray)
                //mMobileListFragment.mMobileListPresenter.selectedSortItem(selectedItem)
                dialog.dismiss()

            } catch (e: IllegalArgumentException) {
                Log.d("errorCatch",e.toString())
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    override fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
        Intent(RECEIVED_TOKEN).let {
            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
            LocalBroadcastManager.getInstance(context).sendBroadcast(it)
            Log.d("sortList",content.toString())
        }

    }

//    override fun recieveFavoriteData(selectedItem: String) {
//        this.selectedItem = selectedItem
//
//        LocalBroadcastManager.getInstance(context).registerReceiver(
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