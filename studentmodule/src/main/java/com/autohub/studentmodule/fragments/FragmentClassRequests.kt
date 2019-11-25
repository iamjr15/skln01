package com.autohub.studentmodule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.ViewPagerAdapter
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentClassRequestsBinding

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


}