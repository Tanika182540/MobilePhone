package com.codemobiles.mobilephone


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.MobileListFragment.Companion.mMobileListPresenter
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.database.AppDatabase
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.presenter.FavListInterface
import com.codemobiles.mymobilephone.presenter.FavListPresenter
import com.codemobiles.mymobilephone.presenter.MobileListPresenter
import com.codemobiles.mymobilephone.presenter.MobileListPresenter.Companion.mMobileArray
import com.codemobiles.mymobilephone.presenter.MobileListPresenter.Companion.sortedList
import kotlinx.android.synthetic.main.custom_list.view.mobileImg
import kotlinx.android.synthetic.main.custom_list.view.textViewRating
import kotlinx.android.synthetic.main.custom_list.view.textViewTitle
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.swipeRefresh
import kotlinx.android.synthetic.main.fragment_mobile_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment() ,FavListInterface.FavListView{


    //var favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var selectedItem:String = "default"

    companion object{
        val favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
        lateinit var mAdapter: CustomAdapter
        lateinit var mFavListPresenter: FavListInterface.FavListPresenter
        var mDataArrayUpdate: ArrayList<MobileBean> = ArrayList<MobileBean>()
        val unSortList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    }

    lateinit var _view:View

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
        mFavListPresenter = FavListPresenter(this, context!!,this@FavoriteFragment)

        mAdapter = CustomAdapter(context!!,favList)
        _view.favRecyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)

            val callback = CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(_view.favRecyclerView)


        }

        //mFavListPresenter.feedData(selectedItem)

//        _view.swipeRefresh.setOnRefreshListener {
//            sortedList.clear()
//            mFavListPresenter.feedData(selectedItem)
//        }

        // mFavListPresenter.feedData(selectedItem)

    }

//    fun feedData(selectedItem: String) {
//        this.selectedItem = selectedItem
//
//        LocalBroadcastManager.getInstance(context!!).registerReceiver(
//            object : BroadcastReceiver(){
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    favList.clear()
//                    favList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    sortedList.clear()
//                    sortedList.addAll(favList)
//                     mAdapter.notifyDataSetChanged()
//                    Log.d("sortFragment",sortedList.toString())
//
//                }
//            },
//            IntentFilter(RECEIVED_NEW_MESSAGE)
//        )
//
//        Log.d("favList",sortedList.toString())
//
//        Handler().postDelayed({
//            //todo
//            view?.swipeRefresh?.isRefreshing = false
//        },3000)
//
//        mAdapter.notifyDataSetChanged()
//    }


    inner class CustomAdapter(val context: Context, private val androidList: ArrayList<MobileBean>) : RecyclerView.Adapter<CustomAdapter.CustomHolder>(), CustomItemTouchHelperListener {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            androidList?.removeAt(position)
            sortedList.removeAt(position)
//            sendBoardcastUpdateList(sortedList)
            mDataArrayUpdate.addAll(sortedList)
            FavoriteFragment.mAdapter.notifyDataSetChanged()
            Log.d("deletefav", mDataArrayUpdate.toString())
//            notifyItemRemoved(position)
//            notifyDataSetChanged()
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

        override fun getItemCount(): Int = sortedList.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {

            val item = sortedList[position]

            holder.titleTextView.text = item.name
            holder.priceTextView.text = item.price.toString()
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context).load(item.thumbImageURL).into(holder.youtubeImageView)


    }

        inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

            val titleTextView : TextView = view.textViewTitle
            val priceTextView : TextView = view.price
            val youtubeImageView : ImageView = view.mobileImg
            val rating: TextView = view.textViewRating

        }
    }


//    private fun sendBoardcastUpdateList(sortedList: ArrayList<MobileBean>) {
//        Intent(RECEIVED_UPDATE).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, sortedList)
//            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
//            Log.d("sortList",sortedList.toString())
//        }
//    }


    inner class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

            val dragFlags = 0
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewHolder?.let {
                listener.onItemDismiss(viewHolder.adapterPosition)
            }
        }

    }

    interface CustomItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean

        fun onItemDismiss(position: Int)
    }

}
