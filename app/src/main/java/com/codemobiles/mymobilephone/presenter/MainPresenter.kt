package com.codemobiles.mymobilephone.presenter

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.codemobiles.mobilephone.FavoriteFragment.Companion.mFavListPresenter
import com.codemobiles.mobilephone.MobileListFragment.Companion.mAdapter
import com.codemobiles.mobilephone.MobileListFragment.Companion.mMobileListPresenter
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.MainActivity
import com.codemobiles.mymobilephone.MainActivity.Companion.mMainPresenter
import com.codemobiles.mymobilephone.MainActivity.Companion.selectedItem
import java.lang.IllegalArgumentException

class MainPresenter(_view: MainInterface.MainView, context: Context) : MainInterface.MainPresenter  {

    val context:Context = context

    override fun showSortDialog() {
        lateinit var dialog: AlertDialog
        // Initialize a new instance of
        val array = arrayOf("Price low to high","Price high to low","Rating 5-1")

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(context)

        builder.setSingleChoiceItems(array,-1) { _, which->
            // Get the dialog selected item
            selectedItem = array[which]

            try {
                mMobileListPresenter.feedData(selectedItem)
                mFavListPresenter.sortData(selectedItem)

                Log.d("selected", selectedItem)

            }catch (e: IllegalArgumentException){
                // Catch the color string parse exception
            }
            // Dismiss the dialog
            dialog.dismiss()
        }
        // Initialize the AlertDialog using builder object
        dialog = builder.create()
        // Finally, display the alert dialog
        dialog.show()
    }
}