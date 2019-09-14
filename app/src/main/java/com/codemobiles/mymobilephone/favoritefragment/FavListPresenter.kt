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

            var selectedList: List<FavoriteEntity>? =  mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            var sortedList: List<FavoriteEntity>? = listOf()

            when (selectedItem) {
                "Rating 5-1" -> {

                    sortedList = selectedList!!.sortedByDescending { it.rating }
                    Toast.makeText(context, "Rating 5-1", Toast.LENGTH_SHORT).show()
                    Log.d("filter1", selectedList.toString())
                }
                "Price low to high" -> {
                    sortedList = selectedList!!.sortedBy{ it.price }
                    Toast.makeText(context, "Price low to high", Toast.LENGTH_SHORT).show()
                    Log.d("filter2", selectedList.toString())
                }
                "Price high to low" -> {
                    sortedList = selectedList!!.sortedByDescending { it.price }
                    Toast.makeText(context, "Price high to low", Toast.LENGTH_SHORT).show()
                    Log.d("filter3", selectedList.toString())
                }
                else -> {
                    sortedList = selectedList!!
                    Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show()
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