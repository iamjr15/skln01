package com.autohub.studentmodule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.AppConstants
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentClassRequestsBinding
import java.util.*

/**
 * Created by Vt Netzwelt
 */

class FragmentClassRequests : BaseFragment() {
    private var mType = "student"
    private var mBinding: FragmentClassRequestsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_class_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentClassRequestsBinding.bind(view)
        fetchUser()
    }

    private fun fetchUser() {
        var root = getString(R.string.db_root_tutors)
        if (mType.equals("student", ignoreCase = true)) {
            root = getString(R.string.db_root_students)
        }
        firebaseStore.collection(root).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserModel::class.java)
                    val adapter = ViewPagerAdapter(childFragmentManager)
                    adapter.addData(getFragmentClassRequests("Latest", user), "Latest")
                    adapter.addData(getFragmentClassRequests("All", user), "All")
                    mBinding!!.tabs.setupWithViewPager(mBinding!!.viewpager)
                    mBinding!!.viewpager.adapter = adapter
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    private fun getFragmentClassRequests(type: String, user: UserModel?): FragmentRequests {
        val latestRequests = FragmentRequests()
        val bundle = Bundle()
        bundle.putString(AppConstants.KEY_TYPE, type)
        bundle.putParcelable(AppConstants.KEY_DATA, user)

        bundle.putString("_user_type", mType)
        latestRequests.arguments = bundle
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