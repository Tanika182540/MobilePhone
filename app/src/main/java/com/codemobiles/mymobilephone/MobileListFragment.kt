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
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.models.MobileBean
import kotlinx.android.synthetic.main.custom_list.view.*
import kotlinx.android.synthetic.main.fragment_mobile_list.view.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.FavoriteFragment.Companion.mDataArrayUpdate
import com.codemobiles.mobilephone.FavoriteFragment.Companion.mFavListPresenter
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.MainActivity.Companion.selectedItem
import com.codemobiles.mymobilephone.presenter.MobileListInterface
import com.codemobiles.mymobilephone.presenter.MobileListPresenter
import com.codemobiles.mymobilephone.presenter.MobileListPresenter.Companion.mMobileArray
import kotlinx.android.synthetic.main.fragment_mobile_list.view.swipeRefresh


class MobileListFragment : Fragment(), MobileListInterface.MobileListView {


    private var sortedUpdateList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    lateinit var _view: View
    //private var selectedItem: String = "default"

    companion object{
        lateinit var mMobileListPresenter: MobileListInterface.MobileListPresenter
        var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
        lateinit var mAdapter: CustomAdapter

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_mobile_list, container, false)

        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMobileListPresenter = MobileListPresenter(this, this@MobileListFragment, context!!)
        mAdapter = CustomAdapter(context!!)
        _view.recyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)
        }

        mMobileListPresenter.feedData(selectedItem)
        // mAdapter.notifyDataSetChanged()

//        mMobileListPresenter.recieveBroadcast(_view)
//
//        _view.swipeRefresh.setOnRefreshListener {
//            mMobileListPresenter.recieveBroadcast(_view)
//        }

        mMobileListPresenter.setupDatabase()
        mMobileListPresenter.setUpWorkerThread()
    }


//    private fun recieveUpdateBroadcast() {
//        LocalBroadcastManager.getInstance(context!!).registerReceiver(
//            object : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    sortedUpdateList.clear()
//                    sortedUpdateList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
//                    mDataArrayUpdate.clear()
//                    mDataArrayUpdate.addAll(sortedUpdateList)
//                    Log.d("updateList", sortedUpdateList.toString())
//                    mAdapter.notifyDataSetChanged()
//
//                }
//            },
//            IntentFilter(RECEIVED_UPDATE)
//        )
//    }

    inner class CustomAdapter(val context: Context) : RecyclerView.Adapter<CustomHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {

            return CustomHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.custom_list,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int = mMobileArray.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = mMobileArray[position]

            holder.titleTextView.text = item.name
            holder.subtitleTextView.text = item.description
            holder.price.text = "Price : $ " + item.price
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)

            holder.favButton.isChecked = false

            Log.d("deletearray",mDataArrayUpdate.toString())
//            mMobileListPresenter.updateFavList(mDataArrayUpdate,item,holder)
            for (i in 0 until sortedUpdateList.size) {
                if (item.name.contentEquals(sortedUpdateList[i].name)) {
                    holder.favButton.isChecked = true
                    MobileListFragment.mAdapter.notifyDataSetChanged()
                    Log.d("deletelist", sortedUpdateList.toString())
                }
            }


            holder.favButton.setOnCheckedChangeListener { button, isChecked ->

                if (isChecked) {
                    mMobileListPresenter.addToFavorite(item, position)
                } else {
                    mMobileListPresenter.removeFavorite(item, position)
                }
            }

            holder.cardView.setOnClickListener {
                mMobileListPresenter.gotoDetailPage(item)
            }
        }
    }

//    fun removeFavorite(item: MobileBean, position: Int) {
//
//        favList.remove(item)
//        Log.d("SCB_NETWORK", favList.toString())
//        sendBroadcastMessage(favList)
//    }

//    fun addToFavorite(item: MobileBean, position: Int) {
//
//        favList.add(item)
//        Log.d("SCB_NETWORK", favList.toString())
//        sendBroadcastMessage(favList)
//
//    }

//    fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
//        Intent(RECEIVED_NEW_MESSAGE).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
//            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
//            Log.d("FavList", content.toString())
//            recieveUpdateBroadcast()
//        }
//    }

    inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView: TextView = view.textViewTitle
        val subtitleTextView: TextView = view.textViewSubTitle
        val youtubeImageView: ImageView = view.mobileImg
        val price: TextView = view.textViewPrice
        val rating: TextView = view.textViewRating
        val cardView: CardView = view.cardView
        val favButton: ToggleButton = view.favButton

        init {
            favButton.text = null
            favButton.textOff = null
            favButton.textOn = null
        }
    }
}