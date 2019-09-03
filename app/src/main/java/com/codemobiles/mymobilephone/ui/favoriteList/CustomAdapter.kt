package com.codemobiles.mymobilephone.ui.favoriteList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.MobileListFragment.Companion.favUpdateList
import com.codemobiles.mobilephone.MobileListFragment.Companion.sortedData
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE
import com.codemobiles.mymobilephone.RECEIVED_UPDATE
import kotlinx.android.synthetic.main.favorite_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(val context: Context, private val listener: FavoriteListListener) : RecyclerView.Adapter<CustomAdapter.CustomHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.favorite_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val item = favUpdateList[position]

        holder.titleTextView.text = item.name
        holder.priceTextView.text = item.price.toString()
        holder.rating.text = "Rating : " + item.rating
        Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)
    }



//    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
//            Collections.swap(androidList, fromPosition, toPosition)
//            notifyItemMoved(fromPosition, toPosition)
//            return true
//        }
//
//    override fun onItemDismiss(position: Int) {
//            androidList?.removeAt(position)
//            sortedData.removeAt(position)
//            //sendBoardcastUpdateList(sortedData)
//            Log.d("delete", sortedData.toString())
////            notifyItemRemoved(position)
//            notifyDataSetChanged()
//        }



        override fun getItemCount(): Int = favUpdateList.size


    interface FavoriteListListener{


    }
    class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

            val titleTextView : TextView = view.textViewTitle
            val priceTextView : TextView = view.price
            val youtubeImageView : ImageView = view.mobileImg
            val rating: TextView = view.textViewRating

        }


//    private fun sendBoardcastUpdateList(sortedList: ArrayList<MobileBean>) {
//        Intent(RECEIVED_UPDATE).let {
//            it.putParcelableArrayListExtra(RECEIVED_MESSAGE, sortedList)
//            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
//            Log.d("sortList",sortedList.toString())
//        }
//    }

    interface CustomItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int) : java.lang.Boolean

        fun onItemDismiss(position: Int)
    }

    class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) : ItemTouchHelper.Callback() {
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
            viewHolder?.let {
                listener.onItemDismiss(viewHolder.adapterPosition)
            }
        }

    }

}
