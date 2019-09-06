package com.codemobiles.mobilephone


import android.content.Context
import android.content.EntityIterator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.presenter.FavListInterface
import com.codemobiles.mymobilephone.presenter.FavListPresenter
import kotlinx.android.synthetic.main.custom_list.view.mobileImg
import kotlinx.android.synthetic.main.custom_list.view.textViewRating
import kotlinx.android.synthetic.main.custom_list.view.textViewTitle
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.swipeRefresh
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment(), FavListInterface.FavListView, SortTypeListener {

    override fun updateSortType(sort: String) {

        mFavListPresenter.sortData(sort)
        mAdapter.notifyDataSetChanged()
    }

    override fun getFav(selectedList: List<FavoriteEntity>?) {
        activity?.runOnUiThread {
            selectedList?.let {
                mAdapter.setData(selectedList)
                mAdapter.notifyDataSetChanged()
            }

        }

        Log.d("listfav", selectedList.toString())
    }

    private var selectedItem: String = "default"

    lateinit var mAdapter: CustomAdapter

    companion object {
        val favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
        lateinit var mFavListPresenter: FavListInterface.FavListPresenter
        var mDataArrayUpdate: ArrayList<MobileBean> = ArrayList<MobileBean>()
    }

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
        mFavListPresenter = FavListPresenter(this, context!!, this@FavoriteFragment)

        mAdapter = CustomAdapter(context!!)
        _view.favRecyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)

            val callback = CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(_view.favRecyclerView)

            mFavListPresenter.setUpWorkerThread()
            mFavListPresenter.setupDatabase()

            mFavListPresenter.sortData(selectedItem)

        }

        _view.swipeRefresh.setOnRefreshListener {
            mFavListPresenter.sortData(selectedItem)
            hideLoading()
        }

    }


    inner class CustomAdapter(val context: Context) : RecyclerView.Adapter<CustomAdapter.CustomHolder>(),
        CustomItemTouchHelperListener {

        var androidList: ArrayList<FavoriteEntity> = arrayListOf()

        fun setData(list: List<FavoriteEntity>) {
            androidList.clear()
            androidList.addAll(list)
            Log.d("clearList", androidList.toString())
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            mFavListPresenter.deleteFavorite(androidList[position].mobileID!!)
            androidList.removeAt(position)
            notifyItemRemoved(position)
            Log.d("deletefav", mDataArrayUpdate.toString())
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {

            return CustomHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.favorite_list,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int = androidList.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {

            val item = androidList[position]

            holder.titleTextView.text = item.name
            holder.priceTextView.text = item.price.toString()
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context).load(item.thumbImageURL).into(holder.youtubeImageView)
        }

        inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

            val titleTextView: TextView = view.textViewTitle
            val priceTextView: TextView = view.price
            val youtubeImageView: ImageView = view.mobileImg
            val rating: TextView = view.textViewRating
        }
    }

    inner class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) :
        ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

            val dragFlags = 0
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewHolder.let {
                listener.onItemDismiss(viewHolder.adapterPosition)
            }
        }

    }

    interface CustomItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

        fun onItemDismiss(position: Int)
    }

}
