package com.autohub.skln.utills

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter  constructor(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mData = ArrayList<Info>()

    override fun getItem(position: Int): Fragment {
        return mData[position].fragment
    }

    override fun getCount(): Int {
        return mData.size
    }

     fun addData(fragment: Fragment, title: String) {
        mData.add(Info(fragment, title))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mData[position].title
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis()
    }

     inner class Info(val fragment: Fragment, val title: String)
}
