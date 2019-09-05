package com.codemobiles.mymobilephone.presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.presenter.MobileListPresenter.Companion.sortedList

class FavListPresenter(
    _view: FavListInterface.FavListView,
    context: Context,
    favoriteFragment: FavoriteFragment
) : FavListInterface.FavListPresenter {

    lateinit var mCMWorkerThread : CMWorkerThread
    var mDatabaseAdapter : AppDatabase? = null
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
        val task = Runnable{

            var selectedList : List<FavoriteEntity>? = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            view?.getFav(selectedList)
        }
        mCMWorkerThread.postTask(task)

        Log.d("favList",sortedList.toString())
        Handler().postDelayed({
            //todo
            view?.hideLoading()
        },3000)

    }


    override fun sortData(selectedItem: String){



        val task = Runnable{

            var selectedList : List<FavoriteEntity>? = listOf()
            val checkId = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()

            Log.d("checkId",checkId.toString())


        when (selectedItem) {
            "Rating 5-1" ->{
                selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortRating()
                sortedList.sortByDescending { it.rating }
                Log.d("filter1", selectedList.toString())
            }

            "Price low to high" -> {
                selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortPriceL()
                sortedList.sortBy { it.price }
                Log.d("filter2", selectedList.toString())
            }
            "Price high to low" ->{
                selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortPriceH()
                sortedList.sortByDescending { it.price }
                Log.d("filter3", selectedList.toString())
            }
            else -> { // Note the block
//                selectedList = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
                sortedList.addAll(sortedList)

                Log.d("filter4", selectedList.toString())
            }
        }
            view?.getFav(selectedList)
//        FavoriteFragment.mAdapter.notifyDataSetChanged()

        }
        mCMWorkerThread.postTask(task)
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