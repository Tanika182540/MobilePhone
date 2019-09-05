package com.codemobiles.mymobilephone.presenter



import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.codemobiles.mobilephone.FavoriteFragment.Companion.favList
import com.codemobiles.mobilephone.MobileDetailActivity
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.database.MobileEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileListPresenter(
    private val _view: MobileListInterface.MobileListView,
    mobileListFragment: MobileListFragment,
    context: Context
) : MobileListInterface.MobileListPresenter{
    override fun loadDatabase() {

    }


//    }

    //    override fun getAllMobile(): MobileEntity? {
//        var mobileList: MobileEntity?
//        val task = Runnable {
//           mobileData = mDatabaseAdapter?.mobileDao()?.queryMobile()!!
//        }
//        return  mobileData
//        mCMWorkerThread.postTask(task)
    val context:Context = context
    val mobileListFragment:MobileListFragment = mobileListFragment
    lateinit var mCMWorkerThread : CMWorkerThread
    var mDatabaseAdapter : AppDatabase? = null

    var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()

    companion object{
        var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()
        lateinit var mobileData:MobileEntity

    }

    override fun addFavoriteButton() {
        val task = Runnable{

            var selectedList : List<FavoriteEntity>? = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            _view?.checkFavoriteButton(selectedList)
        }
        mCMWorkerThread.postTask(task)
    }

    override fun feedData(selectedItem: String) {
        val call = ApiInterface.getClient().getMobileDetail()

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileBean>> {
            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
                //Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)

                    if (mDataArray.isNotEmpty()){
                        for (i in 0 until mDataArray!!.size){
                            val task = Runnable {

                            }
                            mCMWorkerThread.postTask(task)
                        }
                    }

                    Log.d("SCB_NETWORK",mDataArray.toString())
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
                    _view.showData()

                }
            }


        })
        _view.getListMobile(mMobileArray)
        Handler().postDelayed({
            //todo
            _view?.hideLoading()
        },3000)
    }

    override fun removeFavorite(item: MobileBean, position: Int) {
        var a: List<FavoriteEntity>?
        favList.remove(item)
        sortedList.clear()
        sortedList.addAll(favList)
        val task = Runnable{
            mDatabaseAdapter?.favoriteDAO()?.deleteFavorite(item.id)
        }
        mCMWorkerThread.postTask(task)
    }

    override fun addToFavorite(item: MobileBean, position: Int) {

        favList.add(item)
        sortedList.clear()
        sortedList.addAll(favList)

        val task = Runnable{
            val checkId = mDatabaseAdapter?.favoriteDAO()?.queryDuplicateId(item.id)

            if (checkId == null){
                mDatabaseAdapter!!.favoriteDAO().addFavorite(FavoriteEntity(item.id,item.description,item.thumbImageURL,item.name,item.price,item.brand,item.rating))
            }

        }
        mCMWorkerThread.postTask(task)
        Log.d("SCB_NETWORKadd", mDatabaseAdapter.toString())

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