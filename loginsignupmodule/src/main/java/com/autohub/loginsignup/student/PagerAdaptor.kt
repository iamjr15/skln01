package com.autohub.loginsignup.student

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autohub.loginsignup.student.fragments.StudentClassFragment

class PagerAdaptor(fragmentManager: FragmentManager, var fragmentsList: ArrayList<StudentClassFragment>) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    constructor(fragmentManager: FragmentManager, fragmentsList: ArrayList<StudentClassFragment>
                , fragmentsListnew: ArrayList<StudentClassFragment>
    )
            : this(fragmentManager = fragmentManager, fragmentsList = fragmentsList)

    override fun getItem(position: Int): Fragment {
        return fragmentsList[position]
    }

    override fun getPageWidth(position: Int): Float {
        return 0.6f
    }

    override fun getCount(): Int {
        return fragmentsList.size
    }
}