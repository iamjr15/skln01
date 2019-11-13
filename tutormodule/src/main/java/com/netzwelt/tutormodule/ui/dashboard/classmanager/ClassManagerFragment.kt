package com.netzwelt.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.utills.ViewPagerAdapter

import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentClassManagerBinding

/**
 * A simple [Fragment] subclass.
 */
class ClassManagerFragment : Fragment() {

    private lateinit var  mBinding: FragmentClassManagerBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentClassManagerBinding.bind(view)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment()), "Classes Today")
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment()), "All classes")

        mBinding.tabs.setupWithViewPager(mBinding!!.viewpager)
        mBinding.viewpager.adapter = adapter
    }

    private fun getFragmentClassRequests(fragment: Fragment/*, user: User?*/): Fragment {

        return fragment
    }

}
