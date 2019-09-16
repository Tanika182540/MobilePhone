package com.codemobiles.mymobilephone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mymobilephone.models.MobileBean
import com.codemobiles.mymobilephone.R
import kotlinx.android.synthetic.main.custom_list.view.*

class CustomMobileListAdapter(val context: Context,private val listener: MobileListListener) : RecyclerView.Adapter<CustomMobileListHolder>() {

    private var mMobileArray:ArrayList<MobileBean>? = arrayListOf()
    private var favButtonList: List<MobileBean>? = listOf()

    fun getFavList(selectedList: List<MobileBean>?) {
        favButtonList = listOf()
        favButtonList = selectedList
        Log.d("favAdapter", selectedList.toString())
    }

    fun setData(list: List<MobileBean>) {
        mMobileArray?.clear()
        mMobileArray?.addAll(list)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomMobileListHolder {

            return CustomMobileListHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.custom_list,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int = mMobileArray!!.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomMobileListHolder, position: Int) {

            val item = mMobileArray!![position]
            val favoriteItem = MobileBean(
                item.brand,
                item.description,
                item.id,
                item.name,
                item.price,
                item.rating,
                item.thumbImageURL
            )

            Log.d("itemFAv",favoriteItem.name)

            holder.favButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listener.addToFavorite(item)
                } else {
                    listener.removeFavorite(item)
                }
            }

            holder.favButton.isChecked = false
            if (favButtonList?.isNotEmpty()!!){

                for (i in 0 until favButtonList!!.size){
                    if (favoriteItem.name.contentEquals(favButtonList!![i].name)){
                        holder.favButton.isChecked = true
                    }

                }
            }
            holder.titleTextView.text = item.name
            holder.subtitleTextView.text = item.description
            holder.price.text = "Price : $ " + item.price
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context).load(item.thumbImageURL).into(holder.youtubeImageView)
            holder.favButton.text = null
            holder.favButton.textOn = null
            holder.favButton.textOff = null
            holder.cardView.setOnClickListener {
                listener.gotoDetailPage(item)
            }
        }

    interface MobileListListener {
        fun gotoDetailPage(item: MobileBean)
        fun addToFavorite(item: MobileBean)
        fun removeFavorite(item: MobileBean)
    }
}


    class CustomMobileListHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView = view.textViewTitle!!
        val subtitleTextView = view.textViewSubTitle!!
        val youtubeImageView = view.mobileImg!!
        val price = view.textViewPrice!!
        val rating = view.textViewRating!!
        val cardView = view.cardView!!
        val favButton = view.favButton!!

    }

