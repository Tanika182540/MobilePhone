package com.codemobiles.mymobilephone.favoritefragment

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.MobileDetailActivity
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.helper.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.helper.PRICE_H_L
import com.codemobiles.mymobilephone.helper.PRICE_L_H
import com.codemobiles.mymobilephone.helper.RATING_5_1

class FavListPresenter(
    _view: FavListInterface.FavListView,
    private var context: Context,
    private var favoriteFragment: FavoriteFragment
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
    private var view: FavListInterface.FavListView = _view


    override fun feedData(selectedItem: String) {
        val task = Runnable {

            var selectedList: List<FavoriteEntity>? = listOf()
            selectedList = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()

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

            var selectedList: List<FavoriteEntity>? =
                mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            var sortedList: List<FavoriteEntity>? = listOf()

            when (selectedItem) {
                RATING_5_1 -> {

                    sortedList = selectedList!!.sortedByDescending { it.rating }

                    Log.d("filter1", selectedList.toString())
                }
                PRICE_L_H -> {
                    sortedList = selectedList!!.sortedBy { it.price }
                    Log.d("filter2", selectedList.toString())
                }
                PRICE_H_L -> {
                    sortedList = selectedList!!.sortedByDescending { it.price }
                    Log.d("filter3", selectedList.toString())
                }
                else -> {
                    sortedList = selectedList!!
                }
            }
            view.getFav(sortedList)
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
            it.openHelper.readableDatabase
        }
    }

    override fun gotoDetailPage(item: MobileBean) {
        val intent = Intent(context, MobileDetailActivity::class.java)

        intent.putExtra("name", item.name)
        intent.putExtra("brand", item.brand)
        intent.putExtra("description", item.description)
        intent.putExtra("image", item.thumbImageURL)
        intent.putExtra("id", item.id)
        intent.putExtra("rating", item.rating)
        intent.putExtra("price", item.price)
        this.favoriteFragment.startActivity(intent)
    }

}