package com.codemobiles.mymobilephone.presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.FavoriteFragment.Companion.mAdapter
import com.codemobiles.mobilephone.FavoriteFragment.Companion.mFavListPresenter
import com.codemobiles.mobilephone.FavoriteFragment.Companion.unSortList
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.presenter.MobileListPresenter.Companion.sortedList
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavListPresenter(
    _view: FavListInterface.FavListView,
    context: Context,
    favoriteFragment: FavoriteFragment
) : FavListInterface.FavListPresenter {
    val context:Context = context


//    override fun getFavDatabase() {
//        val task = Runnable{
//            mDataFavUpdate = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile() as ArrayList<MobileBean>
//            Log.d("FavBean",mDataFavUpdate.toString())
//        }
//         mCMWorkerThread.postTask(task)
//    }

    private var selectedItem:String = "default"
    private var view: FavListInterface.FavListView = _view


    companion object{
        val unSortFavList: ArrayList<MobileBean> = ArrayList<MobileBean>()
        val favListUpdate:ArrayList<MobileBean> = ArrayList<MobileBean>()
        var showList: ArrayList<MobileBean> = ArrayList<MobileBean>()

    }
    override fun updateFavList(
        mDataArrayUpdate: ArrayList<MobileBean>,
        item: MobileBean,
        holder: MobileListFragment.CustomHolder
    ) {
        for (i in 0 until mDataArrayUpdate.size) {
            if (item.name.contentEquals(mDataArrayUpdate[i].name)) {
                holder.favButton.isChecked = true

                Log.d("deletelist", mDataArrayUpdate.toString())
            }
        }
    }

    override fun feedData(selectedItem: String) {
        this.selectedItem = selectedItem

//        LocalBroadcastManager.getInstance(context!!).registerReceiver(
//            object : BroadcastReceiver(){
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    favList.clear()
//                    favList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    sortedList.clear()
//                    sortedList.addAll(favList)
//                     mAdapter.notifyDataSetChanged()
//                    Log.d("sortFragment",sortedList.toString())
//
//                }
//            },
//            IntentFilter(RECEIVED_NEW_MESSAGE)
//        )

//        MobileListPresenter.mMobileArray.clear()

//        mAdapter.notifyDataSetChanged()


        Log.d("favList",sortedList.toString())
        Handler().postDelayed({
            //todo
            view?.hideLoading()
        },3000)

    }


    override fun sortData(selectedItem: String){
        when (selectedItem) {
            "Rating 5-1" ->{
                sortedList.sortByDescending { it.rating }
                Log.d("filter1", sortedList.toString())
            }

            "Price low to high" -> {
                sortedList.sortBy { it.price }
                Log.d("filter22", sortedList.toString())
            }
            "Price high to low" ->{

                sortedList.sortByDescending { it.price }
                Log.d("filter3", sortedList.toString())
            }
            else -> { // Note the block

                sortedList.addAll(sortedList)

                Log.d("filter4", sortedList.toString())
            }
        }
        FavoriteFragment.mAdapter.notifyDataSetChanged()
    }

}