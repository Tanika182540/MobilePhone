package com.codemobiles.mymobilephone.presenter



import android.content.Context
import android.content.Intent
import android.util.Log
import com.codemobiles.mobilephone.MobileDetailActivity
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.CMWorkerThread
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileListPresenter(
    private val _view: MobileListInterface.MobileListView,
    mobileListFragment: MobileListFragment,
    context: Context
) : MobileListInterface.MobileListPresenter{


    override fun sortData(sort: String, moblieList:ArrayList<MobileBean>) {
        var sortedMobile:ArrayList<MobileBean> = ArrayList<MobileBean>()

            mMobileArray.clear()
            if (mDataArray.isEmpty()){
                mDataArray.addAll(moblieList)
            }
            sortedMobile.clear()
            when (sort) {

                "Rating 5-1" -> {
                    sortedMobile.addAll(mDataArray.sortedByDescending({it.rating}))
                    Log.d("checkId!",sortedMobile.size.toString())

                }
                "Price low to high" -> {
                    sortedMobile.addAll(mDataArray.sortedBy { it.price })
                    Log.d("checkId!",sortedMobile.size.toString())
                }
                "Price high to low" -> {
                    sortedMobile.addAll(mDataArray.sortedByDescending { it.price })
                    Log.d("checkId!",sortedMobile.size.toString())
                }
                else -> { // Note the block
                    sortedMobile.addAll(mDataArray)
                }
            }

            Log.d("DATAMAIN",mDataArray.toString())
        _view.submitSortlist(sortedMobile)
        _view.showData()

    }


    override fun sendTask(task: Runnable) {
        mCMWorkerThread.postTask(task)    }


    val context:Context = context
    val mobileListFragment:MobileListFragment = mobileListFragment
    lateinit var mCMWorkerThread : CMWorkerThread
    var mDatabaseAdapter : AppDatabase? = null

    var favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()

    var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()

    override fun getFavoriteList() {
        val task = Runnable{

            var selectedList= mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            val gson = Gson()
            val json = gson.toJson(selectedList)
            val dataList = gson.fromJson<List<MobileBean>>(json,object : TypeToken<List<MobileBean>>() {}.type)
            _view?.favoriteListData(dataList)
            Log.d("fromFavDB","fav " + selectedList.toString())
        }
        mCMWorkerThread.postTask(task)
    }

    override fun feedData(selectedItem: String):ArrayList<MobileBean>{
        val call = ApiInterface.getClient().getMobileDetail()

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileBean>> {
            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
                Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)
                    _view.showData()
                    _view.submitlist(mDataArray)
                }
            }


        })

        return mDataArray

    }

    override fun removeFavorite(item: MobileBean) {
        val task = Runnable{
            mDatabaseAdapter?.favoriteDAO()?.deleteFavorite(item.id)
        }
        mCMWorkerThread.postTask(task)

        Log.d("SCB_NETWORKremove", mDatabaseAdapter.toString())
    }

    override fun addToFavorite(item: MobileBean) {

        val task = Runnable{
            val checkId = mDatabaseAdapter?.favoriteDAO()?.queryDuplicateId(item.id)

            if (checkId == null){
//                Toast.makeText(context,"Item not duplicate.",Toast.LENGTH_LONG).show()
                mDatabaseAdapter!!.favoriteDAO().addFavorite(FavoriteEntity(item.id,
                    item.description,
                    item.thumbImageURL,
                    item.name,
                    item.price,
                    item.brand,
                    item.rating))
            }

        }
//        _view.showData()
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