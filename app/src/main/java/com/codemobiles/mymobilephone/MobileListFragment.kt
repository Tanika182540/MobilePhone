package com.codemobiles.mobilephone


import android.content.Context
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
import com.codemobiles.mymobilephone.*
import com.codemobiles.mymobilephone.database.FavoriteEntity
import com.codemobiles.mymobilephone.database.MobileEntity
import com.codemobiles.mymobilephone.presenter.MobileListInterface
import com.codemobiles.mymobilephone.presenter.MobileListPresenter
import kotlinx.android.synthetic.main.fragment_mobile_list.view.swipeRefresh


class MobileListFragment : Fragment(), MobileListInterface.MobileListView, SortTypeListener {
    override fun getListMobile(mMobileArray: ArrayList<MobileBean>) {
        this.mMobileArray = mMobileArray
    }

    lateinit var list: MobileEntity
    lateinit var mAdapter: CustomAdapter
    var checkFavButton: List<FavoriteEntity>? = listOf()
    lateinit var mMobileListPresenter: MobileListInterface.MobileListPresenter
    lateinit var _view: View
    var mMobileArray: ArrayList<MobileBean> = ArrayList<MobileBean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_mobile_list, container, false)

        return _view
    }
    override fun showData() {
        mAdapter.notifyDataSetChanged()
    }


    override fun hideLoading() {
        view?.swipeRefresh?.isRefreshing = false
    }
    override fun checkFavoriteButton(selectedList: List<FavoriteEntity>?) {
        checkFavButton = selectedList
        Log.d("favorite", checkFavButton.toString())
    }

    override fun updateSortType(sort: String) {
//        mMobileListPresenter.feedData(sort)
        mMobileArray.clear()
        mMobileArray = mMobileListPresenter.sortData(sort)
        mAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMobileListPresenter = MobileListPresenter(this, this@MobileListFragment, context!!)
        mAdapter = CustomAdapter(context!!)
        _view.recyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
        }


        _view.swipeRefresh.setOnRefreshListener {

            val task = Runnable {
                mMobileArray = mMobileListPresenter.loadDatabase()
            }
            mMobileListPresenter.sendTask(task)
            mAdapter.notifyDataSetChanged()
            hideLoading()

        }
        mMobileListPresenter.setUpWorkerThread()
        mMobileListPresenter.setupDatabase()
        mMobileArray.clear()
        mMobileArray = mMobileListPresenter.feedData("default")

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

        override fun getItemCount(): Int = mMobileArray.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {

            val item = mMobileArray[position]


            holder.titleTextView.text = item.name
            holder.subtitleTextView.text = item.description
            holder.price.text = "Price : $ " + item.price
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)
            mMobileListPresenter.addFavoriteButton()
            var favItem = FavoriteEntity(
                item.id,
                item.description,
                item.thumbImageURL,
                item.name,
                item.price,
                item.brand,
                item.rating
            )
            holder.favButton.isChecked = checkFavButton!!.contains(favItem)


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