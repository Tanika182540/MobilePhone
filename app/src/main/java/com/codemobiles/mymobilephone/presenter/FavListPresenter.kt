package com.codemobiles.mymobilephone.presenter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity

class FavListPresenter(
    _view: FavListInterface.FavListView,
    context: Context,
    favoriteFragment: FavoriteFragment
) : FavListInterface.FavListPresenter {
    override fun deleteFavorite(id: Int) {
        val task = Runnable {
            mDatabaseAdapter?.favoriteDAO()?.deleteFavorite(id)
            Toast.makeText(context, "delete!" + id, Toast.LENGTH_SHORT).show()
        }
        mCMWorkerThread.postTask(task)
    }

    lateinit var mCMWorkerThread: CMWorkerThread
    var mDatabaseAdapter: AppDatabase? = null
    val context: Context = context
    private var selectedItem: String = "default"
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
        val task = Runnable {

            var selectedList: List<FavoriteEntity>? = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            view?.getFav(selectedList)
        }
        mCMWorkerThread.postTask(task)

        Handler().postDelayed({
            //todo
            view?.hideLoading()
        }, 3000)

    }


    override fun sortData(selectedItem: String) {

        val task = Runnable {

            var selectedList: List<FavoriteEntity>? = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            val checkId = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()

            Log.d("checkId", checkId.toString())

            when (selectedItem) {
                "Rating 5-1" -> {
                    selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortRating()
                    Toast.makeText(context, "Rating 5-1", Toast.LENGTH_SHORT).show()
                    Log.d("filter1", selectedList.toString())
                }
                "Price low to high" -> {
                    selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortPriceL()
                    Toast.makeText(context, "Price low to high", Toast.LENGTH_SHORT).show()
                    Log.d("filter2", selectedList.toString())
                }
                "Price high to low" -> {
                    selectedList = mDatabaseAdapter?.favoriteDAO()?.querySortPriceH()
                    Toast.makeText(context, "Price high to low", Toast.LENGTH_SHORT).show()
                    Log.d("filter3", selectedList.toString())
                }
                else -> { // Note the block
//                selectedList = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
                    Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show()
//                Log.d("filter4", selectedList.toString())
                }
            }
            view.getFav(selectedList)
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