package com.codemobiles.mobilephone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemobiles.mobilephone.models.MobileBean
import kotlinx.android.synthetic.main.fragment_mobile_list.view.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.presenter.MobileListInterface
import com.codemobiles.mymobilephone.presenter.MobileListPresenter
import com.codemobiles.mymobilephone.ui.mobilelist.CustomAdapter
import kotlinx.android.synthetic.main.fragment_mobile_list.view.swipeRefresh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MobileListFragment : Fragment(), MobileListInterface.MobileListView {
    override fun hideLoading() {
        view?.swipeRefresh?.isRefreshing = false
    }

    lateinit var mAdapter: CustomAdapter

    companion object {
        var sortedData: ArrayList<MobileBean> = ArrayList<MobileBean>()
        var favUpdateList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    }

    var favUpdateList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var mDataArrayUpdate: ArrayList<MobileBean> = ArrayList<MobileBean>()
    var sortedUpdateList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    lateinit var mMobileListPresenter: MobileListInterface.MobileListPresenter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mobile_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = CustomAdapter(context!!, object : CustomAdapter.MobileListListener {
            override fun addToFavorite(item: MobileBean) {
                mMobileListPresenter.addItemToFavorite(item)
            }

            override fun removeFavorite(item: MobileBean) {
               mMobileListPresenter.removeItemFavorite(item)
            }

            override fun gotoDetailPage(item: MobileBean) {
                mMobileListPresenter.navigateDetailPage(item)
            }

        })
        view.recyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
        }

        mMobileListPresenter = MobileListPresenter(this, this@MobileListFragment, context!!)
        mMobileListPresenter.recieveBroadcast()

        view.swipeRefresh.setOnRefreshListener {
            mMobileListPresenter.recieveBroadcast()
        }

        mMobileListPresenter.feedData(mAdapter)

    }

//    private fun feedData() {
////        this.selectedItem = selectedItem
//
//        val call = ApiInterface.getClient().getMobileDetail()
//
//        //Check Request
//        //Log.d("SCB_NETWORK " , call.request().url().toString())
//
//        //change <YoutubeResponse>
//        call.enqueue(object : Callback<List<MobileBean>> {
//            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
//                //Log.d("SCB_NETWORK " , t.message.toString())
//            }
//
//            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
//                if (response.isSuccessful) {
////                    mDataArray.clear()
////                    mDataArray.addAll(response.body()!!)
////                    Log.d("SCB_NETWORK",mDataArray.toString())
////
////                    sendBroadcastToMain(mDataArray)
//
//                    response.body()?.let {
//                        mAdapter.setData(it)
//                        mAdapter.notifyDataSetChanged()
//                    }
//
//                }
//            }
//        })
//    }


//    fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
//        Intent(RECEIVED_NEW_MESSAGE).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
//            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
//            Log.d("FavList", content.toString())
//            mMobileListPresenter.recieveUpdateBroadcast()
//        }
//    }

//    fun navigateDetailPage(item: MobileBean) {
//        val intent = Intent(getActivity(), MobileDetailActivity::class.java)
//
//        intent.putExtra("name", item.name)
//        intent.putExtra("brand", item.brand)
//        intent.putExtra("description", item.description)
//        intent.putExtra("image", item.thumbImageURL)
//        intent.putExtra("id", item.id)
//        intent.putExtra("rating", item.rating)
//        intent.putExtra("price", item.price)
//        startActivity(intent)
//    }
}