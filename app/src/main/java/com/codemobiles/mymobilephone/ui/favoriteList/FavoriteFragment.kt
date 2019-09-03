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
import com.codemobiles.mobilephone.MobileListFragment.Companion.favUpdateList
import com.codemobiles.mobilephone.MobileListFragment.Companion.sortedData
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.ui.favoriteList.CustomAdapter
import kotlinx.android.synthetic.main.custom_list.view.mobileImg
import kotlinx.android.synthetic.main.custom_list.view.textViewRating
import kotlinx.android.synthetic.main.custom_list.view.textViewTitle
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment() {

    //var favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    lateinit var mAdapter: CustomAdapter
    private var selectedItem:String = "default"
    private lateinit var androidList: ArrayList<MobileBean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val _view:View = inflater.inflate(R.layout.fragment_favorite, container, false)

        mAdapter = CustomAdapter(context!!,object : CustomAdapter.FavoriteListListener{

        })

        _view.favRecyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)

//            val callback = CustomAdapter.CustomItemTouchHelperCallback(mAdapter)
//            val itemTouchHelper = ItemTouchHelper(callback)
//            itemTouchHelper.attachToRecyclerView(_view.favRecyclerView)


        }

        _view.swipeRefresh.setOnRefreshListener {
            feedData(selectedItem)
        }

        feedData(selectedItem)

        Log.d("sortFragment1", favUpdateList.toString())

        return _view
    }

    fun feedData(selectedItem: String) {
        this.selectedItem = selectedItem

        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {

                    favUpdateList.clear()
                    favUpdateList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    sortedData.clear()
                    sortedData.addAll(favUpdateList)
                     mAdapter.notifyDataSetChanged()


                }
            },
            IntentFilter(RECEIVED_NEW_MESSAGE)
        )
        Handler().postDelayed({
            //todo
            view?.swipeRefresh?.isRefreshing = false
        },3000)

        mAdapter.notifyDataSetChanged()
    }



}
