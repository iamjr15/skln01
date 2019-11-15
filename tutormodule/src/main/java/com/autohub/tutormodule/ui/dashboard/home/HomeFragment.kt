package com.autohub.tutormodule.ui.dashboard.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorHomeBinding
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener

class HomeFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorHomeBinding
    private lateinit var homeListner: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_home, container, false)

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentTutorHomeBinding.bind(view)
        mBinding.callback = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListner = context as HomeListener
    }

    fun onpendingrequestClick() {
        homeListner.pendingRequestSelect()

    }

    fun onManageClassClick() {
        homeListner.managerSelected()


    }

}