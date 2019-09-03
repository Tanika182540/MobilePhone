package com.codemobiles.mymobilephone.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.codemobiles.mobilephone.FavoriteFragment
import com.codemobiles.mobilephone.MobileListFragment
import com.codemobiles.mymobilephone.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context,val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    lateinit var mMobileListFragment: MobileListFragment
    lateinit var mFavoriteFragment: FavoriteFragment

    override fun getItem(position: Int): Fragment {

        return when(position){
            //pass params of intent to fragment
            0 -> {
                mMobileListFragment = MobileListFragment()
                mMobileListFragment
            }
            else -> {
                mFavoriteFragment = FavoriteFragment()
                mFavoriteFragment
            }
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return TAB_TITLES.size
    }
}