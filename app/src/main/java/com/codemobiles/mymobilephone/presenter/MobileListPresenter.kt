package com.codemobiles.mymobilephone.presenter


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.MobileDetailActivity
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.MobileListFragment.Companion.sortedData
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE
import com.codemobiles.mymobilephone.RECEIVED_TOKEN
import com.codemobiles.mymobilephone.presenter.MainActivityPresenter.Companion.dataList
import com.codemobiles.mymobilephone.ui.mobilelist.CustomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileListPresenter(
    _view: MobileListInterface.MobileListView,
    mobileListFragment: MobileListFragment,
    context: Context
) : MobileListInterface.MobileListPresenter{

    val view:MobileListInterface.MobileListView = _view
    val context:Context = context
    var mobileFavList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    val mobileListFragment:MobileListFragment = mobileListFragment

    private var selectedItem:String = "default"
    private lateinit var mMobileArray: ArrayList<MobileBean>

    override fun selectedSortItem(
        selectedItem: String,
        mDataArray: ArrayList<MobileBean>,
        mAdapter: CustomAdapter
    ) {

        this.selectedItem = selectedItem

        when (selectedItem) {
            "Rating 5-1" ->{

                dataList.clear()
                dataList.addAll(mDataArray.sortedWith(compareBy({ it.rating })))
                Log.d("MySort1", dataList[1].rating.toString())
                mAdapter.setData(dataList)
                mAdapter.notifyDataSetChanged()

            }

            "Price low to high" -> {

                dataList.clear()
                dataList.addAll(mDataArray.sortedWith(compareBy({ it.price })))
                Log.d("MySort2", dataList[1].price.toString())
                mAdapter.setData(dataList)
                mAdapter.notifyDataSetChanged()
            }
            "Price high to low" ->{

                dataList.clear()
                dataList.addAll( mDataArray.sortedByDescending { it.price })
                Log.d("MySort3", dataList[1].price.toString())
                mAdapter.setData(dataList)
                mAdapter.notifyDataSetChanged()
            }
            else -> { // Note the block

                dataList.clear()
                dataList.addAll(mDataArray)
                Log.d("MySort4", dataList.toString())
                mAdapter.setData(dataList)
                mAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun recieveBroadcast() {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {

                    sortedData.clear()
                    sortedData.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    Log.d("sortList", sortedData.toString())

                }
            },
            IntentFilter(RECEIVED_TOKEN)
        )

        Handler().postDelayed({
            //todo
            view?.hideLoading()
        }, 3000)
    }

//    override fun recieveUpdateBroadcast() {
//        LocalBroadcastManager.getInstance(context).registerReceiver(
//            object : BroadcastReceiver(){
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    mobileFavList.clear()
//                    mobileFavList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    Log.d("MainFav",mobileFavList.toString())
//
//                }
//            },
//            IntentFilter(RECEIVED_NEW_MESSAGE)
//        )
//    }

    override fun removeItemFavorite(item: MobileBean) {

        mobileListFragment.favList.remove(item)

        //sendBroadcastMessage(MobileListFragment.favList)
        mobileListFragment.favUpdateList.addAll(mobileListFragment.favList)
        Log.d("SCB_NETWORKremove", mobileListFragment.favUpdateList.toString())
    }

    override fun addItemToFavorite(item: MobileBean) {
        mobileListFragment.favList.add(item)
        //sendBroadcastMessage(MobileListFragment.favList)
        mobileListFragment.favUpdateList.addAll(mobileListFragment.favList)
        Log.d("SCB_NETWORKadd", mobileListFragment.favUpdateList.toString())
    }

    override fun feedData(mAdapter:CustomAdapter) {
//        this.selectedItem = selectedItem

        val call = ApiInterface.getClient().getMobileDetail()

        //Check Request
        //Log.d("SCB_NETWORK " , call.request().url().toString())

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileBean>> {
            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
                //Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
                if (response.isSuccessful) {
//                    mDataArray.clear()
//                    mDataArray.addAll(response.body()!!)
//                    Log.d("SCB_NETWORK",mDataArray.toString())
//
//                    sendBroadcastToMain(mDataArray)

                    response.body()?.let {
                        mDataArray.clear()
                        mDataArray.addAll(it)
                        mobileListFragment.mMobileListPresenter.selectedSortItem(selectedItem,mDataArray,mAdapter)
//                        mAdapter.setData(it)
//                        mAdapter.notifyDataSetChanged()
                    }

                }
            }
        })
    }

    override fun navigateDetailPage(item: MobileBean) {
        val intent = Intent(context, MobileDetailActivity::class.java)

        intent.putExtra("name", item.name)
        intent.putExtra("brand", item.brand)
        intent.putExtra("description", item.description)
        intent.putExtra("image", item.thumbImageURL)
        intent.putExtra("id", item.id)
        intent.putExtra("rating", item.rating)
        intent.putExtra("price", item.price)
        this.mobileListFragment.startActivity(intent)
    }

}