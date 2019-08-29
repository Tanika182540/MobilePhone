package com.codemobiles.mobilephone


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.*
import kotlinx.android.synthetic.main.custom_list.view.mobileImg
import kotlinx.android.synthetic.main.custom_list.view.textViewRating
import kotlinx.android.synthetic.main.custom_list.view.textViewTitle
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment() {

    var favList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private var sortedList: ArrayList<MobileBean> = ArrayList<MobileBean>()
    lateinit var mAdapter: CustomAdapter
    private var selectedItem:String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val _view:View = inflater.inflate(R.layout.fragment_favorite, container, false)

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



        feedData(selectedItem)



        return _view
    }

    fun feedData(selectedItem: String) {
        this.selectedItem = selectedItem

        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {

                    favList.clear()
                    favList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    sortedList.clear()
                    sortedList.addAll(favList)
                    mAdapter.notifyDataSetChanged()

                }
            },
            IntentFilter(RECEIVED_NEW_MESSAGE)
        )

        when (selectedItem) {
            "Rating 5-1" ->{
                sortedList.clear()
                sortedList.addAll(favList.sortedWith(compareBy({ it.rating })))
                Log.d("MyFilter" , selectedItem)

            }

            "Price low to high" -> {
                sortedList.clear()
                sortedList.addAll(favList.sortedWith(compareBy({ it.price })))
                Log.d("MyFilter" , selectedItem)

            }
            "Price high to low" ->{
                sortedList.clear()
                sortedList.addAll( favList.sortedByDescending { it.price })
                Log.d("MyFilter" , selectedItem)

            }
            else -> { // Note the block
                sortedList.clear()
                sortedList.addAll(favList)
                Log.d("MyFilter" , selectedItem)

            }
        }

        mAdapter.notifyDataSetChanged()
    }


    inner class CustomAdapter(val context: Context, private val androidList: ArrayList<MobileBean>) : RecyclerView.Adapter<CustomAdapter.CustomHolder>(), CustomItemTouchHelperListener {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            androidList?.removeAt(position)
            notifyItemRemoved(position)
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

        override fun getItemCount(): Int = favList.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = sortedList[position]

            holder.titleTextView.text = item.name
            holder.priceTextView.text = item.price.toString()
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)


    }

        inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

            val titleTextView : TextView = view.textViewTitle
            val priceTextView : TextView = view.price
            val youtubeImageView : ImageView = view.mobileImg
            val rating: TextView = view.textViewRating

        }
    }



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
