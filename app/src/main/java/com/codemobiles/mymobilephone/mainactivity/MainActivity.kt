package com.codemobiles.mymobilephone.mainactivity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import com.codemobiles.mymobilephone.R
import com.codemobiles.mymobilephone.helper.PRICE_H_L
import com.codemobiles.mymobilephone.helper.PRICE_L_H
import com.codemobiles.mymobilephone.helper.RATING_5_1
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity() {

    lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        loadingDialog()
        sortButton.setOnClickListener {
            showSortDialog()
        }

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {}
        })
    }

    private fun showSortDialog() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(PRICE_L_H , PRICE_H_L, RATING_5_1)

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

    private fun loadingDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        Handler().postDelayed({dialog.dismiss()},1000)
    }
}
