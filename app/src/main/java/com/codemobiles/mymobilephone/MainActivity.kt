package com.codemobiles.mymobilephone

import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import com.codemobiles.mymobilephone.ui.main.SectionsPagerAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        sortButton.setOnClickListener {
            lateinit var dialog:AlertDialog
            // Initialize a new instance of
            val array = arrayOf("Price low to high","Price high to low","Rating 5-1")

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(this)

            builder.setSingleChoiceItems(array,-1) { _, which->
                // Get the dialog selected item
                val selectedItem = array[which]

                try {
                    sectionsPagerAdapter.mMobileListFragment.feedData(selectedItem)
                    dialog.dismiss()

                }catch (e:IllegalArgumentException){
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


}
