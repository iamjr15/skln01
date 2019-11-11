package com.netzwelt.studentmodule.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.netzwelt.studentmodule.R
import com.netzwelt.studentmodule.databinding.FragmentMyClassesBinding
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class MyClassesFragment : Fragment() {
    private var mBinding: FragmentMyClassesBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentMyClassesBinding.bind(view)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addData(getFragmentClassRequests("Schedule"/*, user*/, ScheduleFragment()), "Schedule")
        adapter.addData(getFragmentClassRequests("Enrolled classes"/*, user*/, EnrolledClassesFragment()), "Enrolled classes")

        mBinding!!.tabs.setupWithViewPager(mBinding!!.viewpager)
        mBinding!!.viewpager.adapter = adapter
    }

    private fun getFragmentClassRequests(type: String, fragment: Fragment/*, user: User?*/): Fragment {
        /* val bundle = Bundle()
         bundle.putString(AppConstants.KEY_TYPE, type)
         bundle.putParcelable(AppConstants.KEY_DATA, user)*/
        var root = "Student"

        //    bundle.putString("_user_type", mType)
        //  latestRequests.arguments = bundle
        return fragment
    }


    private class ViewPagerAdapter internal constructor(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mData = ArrayList<Info>()

        override fun getItem(position: Int): Fragment {
            return mData[position].fragment
        }

        override fun getCount(): Int {
            return mData.size
        }

        internal fun addData(fragment: Fragment, title: String) {
            mData.add(Info(fragment, title))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mData[position].title
        }

        override fun getItemId(position: Int): Long {
            return System.currentTimeMillis()
        }

        internal inner class Info(val fragment: Fragment, val title: String)
    }

}
