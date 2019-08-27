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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import kotlinx.android.synthetic.main.custom_list.view.*
import kotlinx.android.synthetic.main.fragment_mobile_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.R.attr.data
import com.codemobiles.mymobilephone.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MobileListFragment : Fragment() {

    private var mDataArray: ArrayList<MobileBean> = ArrayList<MobileBean>()
    private lateinit var mAdapter: CustomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val _view :View = inflater.inflate(R.layout.fragment_mobile_list, container, false)

        mAdapter = CustomAdapter(context!!)
        _view.recyclerView.let {
            it.adapter = mAdapter

            //IMPORTANT ! ! ! ! ! !
            it.layoutManager = LinearLayoutManager(activity)
            //it.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL,false)
            //it.layoutManager = GridLayoutManager(activity,2)
        }
        feedData()

        return _view
    }

    fun feedData() {
        val call = ApiInterface.getClient().getMobileDetail()

        //Check Request
        Log.d("SCB_NETWORK " , call.request().url().toString())

        //change <YoutubeResponse>
        call.enqueue(object : Callback<List<MobileBean>> {
            override fun onFailure(call: Call<List<MobileBean>>, t: Throwable) {
                Log.d("SCB_NETWORK " , t.message.toString())
            }

            override fun onResponse(call: Call<List<MobileBean>>, response: Response<List<MobileBean>>) {
                if(response.isSuccessful){
                    mDataArray.clear()
                    mDataArray.addAll(response.body()!!)
                    Log.d("SCB_NETWORK",mDataArray.toString())

                    mAdapter.notifyDataSetChanged()
                }

            }


        })
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

        override fun getItemCount(): Int = mDataArray.size

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = mDataArray[position]

            holder.titleTextView.text = item.name
            holder.subtitleTextView.text = item.description
            holder.price.text = "Price : $ " + item.price
            holder.rating.text = "Rating : " + item.rating
            Glide.with(context!!).load(item.thumbImageURL).into(holder.youtubeImageView)
            holder.favButton.setOnClickListener {
                addToFavorite()
                holder.favButton.setImageResource(R.drawable.ic_favorite_black_24dp)
            }
            holder.cardView.setOnClickListener {
                gotoDetailPage(item)
            }


        }

    }

    fun addToFavorite(){
        Toast.makeText(
            activity, "Hi! Nampetch",
            Toast.LENGTH_LONG
        ).show()

    }

    fun gotoDetailPage(item: MobileBean) {
        val intent = Intent (getActivity(), MobileDetailActivity::class.java)
        intent.putExtra("name", item.name)
        intent.putExtra("brand",item.brand)
        intent.putExtra("description",item.description)
        intent.putExtra("image",item.thumbImageURL)
        startActivity(intent)
    }

    inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView : TextView = view.textViewTitle
        val subtitleTextView : TextView = view.textViewSubTitle
        val youtubeImageView : ImageView = view.mobileImg
        val price: TextView = view.textViewPrice
        val rating: TextView = view.textViewRating
        val cardView : CardView = view.cardView
        val favButton : ImageView = view.favButton

    }




}


