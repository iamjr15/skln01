package com.autohub.loginsignup.student.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var fragmentsList: ArrayList<Fragment> = ArrayList()

    constructor(fragmentManager: FragmentManager, fragmentsList: ArrayList<Fragment>) : this(fragmentManager) {
        this.fragmentsList = fragmentsList
    }


    // 2
    override fun getItem(position: Int): Fragment {
        return fragmentsList[position]
    }

    override fun getPageWidth(position: Int): Float {
        return 0.6f
    }

    // 3
    override fun getCount(): Int {
        return fragmentsList.size
    }
}