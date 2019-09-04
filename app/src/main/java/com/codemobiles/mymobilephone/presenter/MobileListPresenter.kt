package com.codemobiles.mymobilephone.presenter



import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.FavoriteFragment.Companion.favList
import com.codemobiles.mobilephone.MobileDetailActivity
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.MobileListFragment.Companion.mAdapter
import com.codemobiles.mobilephone.MobileListFragment.Companion.mDataArray
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE
import com.codemobiles.mymobilephone.RECEIVED_TOKEN
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.presenter.FavListPresenter.Companion.showList
import kotlinx.android.synthetic.main.fragment_mobile_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileListPresenter(
    _view: MobileListInterface.MobileListView,
    mobileListFragment: MobileListFragment,
    context: Context
) : MobileListInterface.MobileListPresenter{

    val context:Context = context
    val mobileListFragment:MobileListFragment = mobileListFragment
    lateinit var mCMWorkerThread : CMWorkerThread
    var mDatabaseAdapter : AppDatabase? = null

    companion object{
        var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
        var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()

    }

    override fun feedData(selectedItem: String) {
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
                    Log.d("SCB_NETWORK",mDataArray.toString())
//                    val task = Runnable{
//
//                        for (i in 0 until mDataArray.size) {
//                                mDatabaseAdapter!!.mobileDao().addFavorite(MobileEntity(null, mDataArray[i].id,
//                                    mDataArray[i].description, mDataArray[i].thumbImageURL, mDataArray[i].name,
//                                    mDataArray[i].price, mDataArray[i].brand, mDataArray[i].rating))
//
//                        }
//
//                    }
//                    mCMWorkerThread.postTask(task)
                    mMobileArray.clear()

                    when (selectedItem) {
                        "Rating 5-1" ->{

                            mMobileArray.addAll(mDataArray.sortedWith(compareBy({ it.rating })))
                            Log.d("filter1", mMobileArray.toString())

                        }

                        "Price low to high" -> {

                            mMobileArray.addAll(mDataArray.sortedWith(compareBy({ it.price })))
                            Log.d("filter2", mMobileArray.toString())
                        }
                        "Price high to low" ->{

                            mMobileArray.addAll( mDataArray.sortedByDescending { it.price })
                            Log.d("filter3", mMobileArray.toString())
                        }
                        else -> { // Note the block

                            mMobileArray.addAll(mDataArray)
                            Log.d("filter4", mMobileArray.toString())
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun removeFavorite(item: MobileBean, position: Int) {

        var a: List<FavoriteEntity>?
        favList.remove(item)
        sortedList.clear()
        sortedList.addAll(favList)

        val task = Runnable{
            mDatabaseAdapter?.favoriteDAO()?.deleteFavorite(item.id)
            a = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            Log.d("favBeanRem", a.toString())
        }
        mCMWorkerThread.postTask(task)
        //        mFavListPresenter.getAddFav(sortedList)

//        sendBroadcastMessage(MobileListFragment.favList)
        FavoriteFragment.mAdapter.notifyDataSetChanged()
    }

    override fun addToFavorite(item: MobileBean, position: Int) {

        favList.add(item)
        sortedList.clear()
        sortedList.addAll(favList)

        val task = Runnable{
            val checkId = mDatabaseAdapter?.favoriteDAO()?.queryDuplicateId(item.id)

            if (checkId == null){
                mDatabaseAdapter!!.favoriteDAO().addFavorite(FavoriteEntity(null, item.id,item.description,item.thumbImageURL,item.name,item.price,item.brand,item.rating))
            }

        }
        mCMWorkerThread.postTask(task)
//        mFavListPresenter.getAddFav(sortedList)
        Log.d("SCB_NETWORKadd", mDatabaseAdapter.toString())
//        sendBroadcastMessage(MobileListFragment.favList)
        FavoriteFragment.mAdapter.notifyDataSetChanged()


    }

//    override fun recieveBroadcast(view:View) {
//        LocalBroadcastManager.getInstance(context!!).registerReceiver(
//            object : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) {
//
////                    sortedList.clear()
////                    sortedList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    mDataArray.clear()
//                    mDataArray.addAll(sortedList)
//                    Log.d("sortList", sortedList.toString())
//                    mAdapter.notifyDataSetChanged()
//
//                }
//            },
//            IntentFilter(RECEIVED_TOKEN)
//        )
//
//        Handler().postDelayed({
//            //todo
//            view?.swipeRefresh?.isRefreshing = false
//        }, 3000)
//    }

    override fun gotoDetailPage(item: MobileBean) {
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

    override fun setUpWorkerThread() {
        mCMWorkerThread = CMWorkerThread("scb_database").also {
            it.start()
        }
    }

    override fun setupDatabase() {
        mDatabaseAdapter = AppDatabase.getInstance(context).also {
            // Instance does not create the database.
            // It will do so once call writableDatabase or readableDatabase
            it.openHelper.readableDatabase
        }
    }

}