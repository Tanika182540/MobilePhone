package com.codemobiles.mymobilephone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.CustomItemTouchHelperListener
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.database.FavoriteEntity
import kotlinx.android.synthetic.main.favorite_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class CustomFavoriteAdapter(val context: Context,private val listener: FavListListener) : RecyclerView.Adapter<CustomFavHolder>(),
    CustomItemTouchHelperListener {

    var mDataArrayUpdate: ArrayList<MobileBean> = ArrayList<MobileBean>()
    override fun onBindViewHolder(holder: CustomFavHolder, position: Int) {
        val item = androidList[position]

        holder.titleTextView.text = item.name
        holder.priceTextView.text = item.price.toString()
        holder.rating.text = "Rating : " + item.rating
        Glide.with(context).load(item.thumbImageURL).into(holder.youtubeImageView)
    }


    var androidList: ArrayList<FavoriteEntity> = arrayListOf()

        fun setData(list: List<FavoriteEntity>) {
            androidList.clear()
            androidList.addAll(list)
            notifyDataSetChanged()
            Log.d("clearList", androidList.toString())
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            listener.removeFavorite(androidList[position].mobileID!!)
            androidList.removeAt(position)
            notifyItemRemoved(position)
            Log.d("deletefav", mDataArrayUpdate.toString())
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomFavHolder {

            return CustomFavHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.favorite_list,
                    parent,
                    false
                )
            )
        }

 override fun getItemCount(): Int = androidList.size
    interface FavListListener {
        fun gotoDetailPage(item: MobileBean)
        fun removeFavorite(item: Int)
    }
    }

class CustomFavHolder(view: View) : RecyclerView.ViewHolder(view) {

    val titleTextView: TextView = view.textViewTitle
    val priceTextView: TextView = view.price
    val youtubeImageView: ImageView = view.mobileImg
    val rating: TextView = view.textViewRating
}


