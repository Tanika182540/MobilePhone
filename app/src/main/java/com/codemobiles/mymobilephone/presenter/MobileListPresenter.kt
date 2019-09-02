package com.codemobiles.mymobilephone.presenter


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mobilephone.MobileListFragment.Companion.mAdapter
import com.codemobiles.mobilephone.MobileListFragment.Companion.sortedList
import com.codemobiles.mymobilephone.RECEIVED_MESSAGE
import com.codemobiles.mymobilephone.RECEIVED_TOKEN

class MobileListPresenter(
    _view: MobileListInterface.MobileListView,
    mobileListFragment: MobileListFragment,
    context: Context
) : MobileListInterface.MobileListPresenter{

    val view:MobileListInterface.MobileListView = _view
    val context:Context = context

    override fun recieveBroadcast() {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {

                    sortedList.clear()
                    sortedList.addAll(intent.getParcelableArrayListExtra(RECEIVED_MESSAGE))
                    Log.d("sortList", sortedList.toString())
                    mAdapter.notifyDataSetChanged()

                }
            },
            IntentFilter(RECEIVED_TOKEN)
        )

        Handler().postDelayed({
            //todo
            view?.hideLoading()
        }, 3000)
    }

    override fun recieveUpdateBroadcast() {
    }

}