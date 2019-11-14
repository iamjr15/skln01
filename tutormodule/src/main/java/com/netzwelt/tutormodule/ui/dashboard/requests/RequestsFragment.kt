package com.netzwelt.tutormodule.ui.dashboard.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.User
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentRequestsBinding
import java.util.*

class RequestsFragment : BaseFragment() {
    private var mBinding: FragmentRequestsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_requests, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding = FragmentRequestsBinding.bind(view)

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addData(getFragmentClassRequests("Latest", User()), "Latest")
        adapter.addData(getFragmentClassRequests("All", User()), "All")
        mBinding!!.tabs.setupWithViewPager(mBinding!!.viewpager)
        mBinding!!.viewpager.adapter = adapter
    }

    private fun getFragmentClassRequests(type: String, user: User?): Fragment {
        val latestRequests = RequestsListFragment()
        /*val bundle = Bundle()
        bundle.putString(AppConstants.KEY_TYPE, type)
        bundle.putParcelable(AppConstants.KEY_DATA, user)

        bundle.putString("_user_type", mType)
        latestRequests.arguments = bundle*/
        return latestRequests
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