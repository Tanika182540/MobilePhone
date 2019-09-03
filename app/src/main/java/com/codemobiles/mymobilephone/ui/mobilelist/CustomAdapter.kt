package com.codemobiles.mymobilephone.ui.mobilelist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.R
import kotlinx.android.synthetic.main.custom_list.view.*

class CustomAdapter(val context: Context, private val listener: MobileListListener) : RecyclerView.Adapter<CustomAdapter.CustomHolder>() {

    var mData: List<MobileBean> = listOf()

    fun setData(list: List<MobileBean>) {
        mData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val item = mData[position]

        Log.d("myItem", item.toString())

        holder.titleTextView.text = item.name
        holder.subtitleTextView.text = item.description
        holder.price.text = "Price : $ " + item.price
        holder.rating.text = "Rating : " + item.rating
        Glide.with(context).load(item.thumbImageURL).into(holder.youtubeImageView)

        holder.favButton.isChecked = false


//        for (i in 0 until mDataArrayUpdate.size) {
//            if (item.name.contentEquals(mDataArrayUpdate[i].name)) {
//                holder.favButton.isChecked = true
//            }
//        }
//
        holder.favButton.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                listener.addToFavorite(item)

            } else {
                listener.removeFavorite(item)
            }
        }

        holder.cardView.setOnClickListener {
            listener.gotoDetailPage(item)
        }
    }

    interface MobileListListener {
        fun gotoDetailPage(item:MobileBean)
        fun addToFavorite(item:MobileBean)
        fun removeFavorite(item:MobileBean)
    }


    class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

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
