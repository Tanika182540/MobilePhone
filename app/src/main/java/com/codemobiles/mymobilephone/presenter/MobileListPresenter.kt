package com.codemobiles.mymobilephone.presenter



import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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


    override fun sortData(sort: String):ArrayList<MobileBean> {
        var sortedMobile:ArrayList<MobileBean> = ArrayList<MobileBean>()
        val task = Runnable {

            var selectedList: List<MobileEntity>? = listOf()
            selectedList = mDatabaseAdapter?.mobileDao()?.querySortMobile()
            val checkId = mDatabaseAdapter?.mobileDao()?.querySortMobile()


            mMobileArray.clear()
            if (mDataArray.isEmpty()){
                mDataArray.addAll(selectedList?.get(0)!!.phoneList)
            }
            Log.d("checkId",selectedList?.get(0)!!.phoneList.size.toString())
            var text:List<MobileBean> = listOf()
            sortedMobile.clear()
            when (sort) {

                "Rating 5-1" -> {
                    sortedMobile.clear()
                    Log.d("checkId!",sortedMobile.size.toString())
                    sortedMobile.addAll(mDataArray.sortedByDescending({it.rating}))
                    Log.d("checkId!",sortedMobile.size.toString())

                }
                "Price low to high" -> {
                    sortedMobile.clear()
                    sortedMobile.addAll(mDataArray.sortedBy { it.price })
                    Log.d("checkId!",sortedMobile.size.toString())
                }
                "Price high to low" -> {
                    sortedMobile.clear()
                    sortedMobile.addAll(mDataArray.sortedByDescending { it.price })
                    Log.d("checkId!",sortedMobile.size.toString())
                }
                else -> { // Note the block
//                selectedList = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
//                    Toast.makeText(context, "Default", Toast.LENGTH_SHORT).show()
//                Log.d("filter4", selectedList.toString())
                }
            }

            Log.d("DATAMAIN",mDataArray.toString())
        }
        mCMWorkerThread.postTask(task)
        return sortedMobile
    }


    override fun sendTask(task: Runnable) {
        mCMWorkerThread.postTask(task)    }

    override fun loadDatabase():ArrayList<MobileBean>{

        var loadData: ArrayList<MobileEntity>? = null
        return mDatabaseAdapter?.let{
            it.mobileDao().queryMobile()?.phoneList
        }?:run {
            arrayListOf<MobileBean>()
        }
        Log.d("MYDATAAAAAA",mDatabaseAdapter?.mobileDao()?.queryMobile().toString())

    }

    val context:Context = context
    val mobileListFragment:MobileListFragment = mobileListFragment
    lateinit var mCMWorkerThread : CMWorkerThread
    var mDatabaseAdapter : AppDatabase? = null

    var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()

    var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()

    override fun addFavoriteButton() {
        val task = Runnable{

            var selectedList : List<FavoriteEntity>? = mDatabaseAdapter?.favoriteDAO()?.queryFavMobile()
            _view?.checkFavoriteButton(selectedList)
        }
        mCMWorkerThread.postTask(task)
    }

    override fun feedData(selectedItem: String):ArrayList<MobileBean>{
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
                                val mobileEntity = MobileEntity(null, mDataArray)
                                mDatabaseAdapter?.mobileDao()?.clearMobileList()
//                                var mobileEntity = MobileEntity(mDataArray[i].id,mDataArray[i].description,mDataArray[i].thumbImageURL,mDataArray[i].name,mDataArray[i].price,mDataArray[i].brand,mDataArray[i].rating )
                                mDatabaseAdapter?.mobileDao()?.insertMobileList(mobileEntity)


                            }
                            mCMWorkerThread.postTask(task)
                        }
                    }

                    _view.showData()

                }
            }


        })
        _view.getListMobile(mMobileArray)

        return mDataArray

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