package com.codemobiles.mobilephone


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.adapter.CustomFavoriteAdapter
import com.codemobiles.mymobilephone.adapter.CustomItemTouchHelperCallback
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.favoritefragment.FavListInterface
import com.codemobiles.mymobilephone.favoritefragment.FavListPresenter
import com.codemobiles.mymobilephone.helper.SortTypeListener
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.swipeRefresh
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment(), FavListInterface.FavListView,
    SortTypeListener {

    override fun updateSortType(sort: String) {

        mFavListPresenter.sortData(sort)
        mAdapter.notifyDataSetChanged()
    }

    override fun getFav(selectedList: List<FavoriteEntity>?) {
        activity?.runOnUiThread {
            selectedList?.let {
                mAdapter.setData(selectedList)
            }

        }

        Log.d("listfav", selectedList.toString())
    }

    private var selectedItem: String = "default"

    lateinit var mAdapter: CustomFavoriteAdapter
    lateinit var mFavListPresenter: FavListInterface.FavListPresenter

    lateinit var _view: View

    override fun hideLoading() {
        view?.swipeRefresh?.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_favorite, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mFavListPresenter = FavListPresenter(
            this,
            context!!,
            this@FavoriteFragment
        )

        mAdapter = CustomFavoriteAdapter(context!!,object :CustomFavoriteAdapter.FavListListener{
            override fun gotoDetailPage(item: MobileBean) {
                mFavListPresenter.gotoDetailPage(item)
            }

            override fun removeFavorite(item: Int) {
                mFavListPresenter.deleteFavorite(item)
            }

        })
        _view.favRecyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)

            val callback =
                CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(_view.favRecyclerView)

            mFavListPresenter.setUpWorkerThread()
            mFavListPresenter.setupDatabase()
            mFavListPresenter.feedData("default")

        }



        _view.swipeRefresh.setOnRefreshListener {
            mFavListPresenter.sortData(selectedItem)
            hideLoading()
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }
}