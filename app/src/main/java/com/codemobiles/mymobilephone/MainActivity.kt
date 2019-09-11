package com.codemobiles.mymobilephone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mobilephone.network.ApiInterface
import com.codemobiles.mymobilephone.presenter.MainInterface
import com.codemobiles.mymobilephone.presenter.MainPresenter
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity(), MainInterface.MainView {

    lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)


        sortButton.setOnClickListener {
            showSortDialog()
        }

    }

    private fun showSortDialog() {
        lateinit var dialog: AlertDialog
        val array = arrayOf("Price low to high", "Price high to low", "Rating 5-1")

        val builder = AlertDialog.Builder(this)

        builder.setSingleChoiceItems(array, -1) { _, which ->
           val  selectedItem = array[which]

            try {
                sectionsPagerAdapter.updateSort(selectedItem)

                Log.d("selected", selectedItem)

            } catch (e: IllegalArgumentException) {
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
