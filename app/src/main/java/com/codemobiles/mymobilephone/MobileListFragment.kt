package com.codemobiles.mobilephone


import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.presenter.MainActivityPresenter.Companion.sortedArrayList
import com.codemobiles.mymobilephone.presenter.MobileListInterface
import com.codemobiles.mymobilephone.presenter.MobileListPresenter
import kotlinx.android.synthetic.main.fragment_mobile_list.view.swipeRefresh


class MobileListFragment : Fragment(), MobileListInterface.MobileListView {
    override fun hideLoading() {
        view?.swipeRefresh?.isRefreshing = false
    }

    companion object{
        lateinit var mAdapter: CustomAdapter
        var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    }

    var mDataArrayUpdate: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var sortedUpdateList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    lateinit var mMobileListPresenter : MobileListInterface.MobileListPresenter
    val favList: ArrayList<MobileBean> = ArrayList<MobileBean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val _view: View = inflater.inflate(R.layout.fragment_mobile_list, container, false)

        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = CustomAdapter(context!!)
        view.recyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)
        }

        mMobileListPresenter = MobileListPresenter(this,this@MobileListFragment, context!!)


        mMobileListPresenter.recieveBroadcast()

        view.swipeRefresh.setOnRefreshListener {
            mMobileListPresenter.recieveBroadcast()
        }

    }



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

        override fun getItemCount(): Int = sortedArrayList.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = sortedArrayList[position]

            Log.d("myItem",item.toString())

            holder.titleTextView.text = item.name
            holder.subtitleTextView.text = item.description
            holder.price.text = "Price : $ " + item.price
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)

            holder.favButton.isChecked = false

            for (i in 0 until mDataArrayUpdate.size) {
                if (item.name.contentEquals(mDataArrayUpdate[i].name)) {
                    holder.favButton.isChecked = true
                }
            }

            holder.favButton.setOnCheckedChangeListener { button, isChecked ->

                if (isChecked) {
                    addToFavorite(item, position)
                } else {
                    removeFavorite(item, position)
                }
            }

            holder.cardView.setOnClickListener {
                gotoDetailPage(item)
            }
        }
    }

    fun removeFavorite(item: MobileBean, position: Int) {

        favList.remove(item)
        Log.d("SCB_NETWORK", favList.toString())
        sendBroadcastMessage(favList)
    }

    fun addToFavorite(item: MobileBean, position: Int) {

        favList.add(item)
        Log.d("SCB_NETWORK", favList.toString())
        sendBroadcastMessage(favList)

    }

    fun sendBroadcastMessage(content: ArrayList<MobileBean>) {
        Intent(RECEIVED_NEW_MESSAGE).let {
            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, content)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
            Log.d("FavList", content.toString())
            mMobileListPresenter.recieveUpdateBroadcast()
        }
    }

    fun gotoDetailPage(item: MobileBean) {
        val intent = Intent(getActivity(), MobileDetailActivity::class.java)

        intent.putExtra("name", item.name)
        intent.putExtra("brand", item.brand)
        intent.putExtra("description", item.description)
        intent.putExtra("image", item.thumbImageURL)
        intent.putExtra("id", item.id)
        intent.putExtra("rating", item.rating)
        intent.putExtra("price", item.price)
        startActivity(intent)
    }

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