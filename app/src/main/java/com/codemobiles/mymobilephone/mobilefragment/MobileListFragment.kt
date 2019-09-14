package com.codemobiles.mobilephone


import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.adapter.CustomMobileListAdapter
import com.codemobiles.mymobilephone.database.MobileEntity
import com.codemobiles.mymobilephone.helper.SortTypeListener
import com.codemobiles.mymobilephone.mobilefragment.MobileListInterface
import com.codemobiles.mymobilephone.mobilefragment.MobileListPresenter
import kotlinx.android.synthetic.main.fragment_mobile_list.*
import kotlinx.android.synthetic.main.fragment_mobile_list.view.swipeRefresh
import android.widget.LinearLayout
import android.view.WindowManager
import android.widget.TextView
import android.view.Gravity
import android.widget.ProgressBar




class MobileListFragment : Fragment(), MobileListInterface.MobileListView,
    SortTypeListener {
    override fun submitSortlist(mobilelist: ArrayList<MobileBean>) {
        mAdapter.setData(mobilelist)
        mAdapter.notifyDataSetChanged()
    }

    override fun submitlist(mobilelist: ArrayList<MobileBean>) {
        mAdapter.setData(mobilelist)
        mAdapter.notifyDataSetChanged()
    }


    lateinit var list: MobileEntity
    lateinit var mAdapter: CustomMobileListAdapter
    var favButtonList: List<MobileBean>? = listOf()
    lateinit var mMobileListPresenter: MobileListInterface.MobileListPresenter
    var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_mobile_list, container, false)
    }
    override fun showData() {
        mAdapter.notifyDataSetChanged()
    }


    override fun hideLoading() {
        view?.swipeRefresh?.isRefreshing = false
    }
    override fun favoriteListData(selectedList: List<MobileBean>?) {
        mAdapter.getFavList(selectedList)
    }

    override fun updateSortType(sort: String) {
        mMobileListPresenter.getFavoriteList()
        mMobileListPresenter.sortData(sort,mMobileArray)
        mAdapter.notifyDataSetChanged()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMobileListPresenter = MobileListPresenter(
            this,
            this@MobileListFragment,
            context!!
        )
        mAdapter = CustomMobileListAdapter(context!!, object : CustomMobileListAdapter.MobileListListener {
            override fun gotoDetailPage(item: MobileBean) {
                mMobileListPresenter.gotoDetailPage(item)
            }

            override fun addToFavorite(item: MobileBean) {

                Log.d("favItem","add " +  item.name)
                mMobileListPresenter.addToFavorite(item)
            }

            override fun removeFavorite(item: MobileBean) {
                Log.d("favItem","remove " + item.name)
                mMobileListPresenter.removeFavorite(item)
            }

        })
        recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)
        }


        swipeRefresh.setOnRefreshListener {
            mMobileArray.clear()
            mMobileListPresenter.getFavoriteList()
            mMobileArray = mMobileListPresenter.feedData("default")
            hideLoading()

        }
        mMobileListPresenter.setUpWorkerThread()
        mMobileListPresenter.setupDatabase()
        mMobileArray.clear()
        mMobileListPresenter.getFavoriteList()
        mMobileListPresenter.feedData("default")

    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

}