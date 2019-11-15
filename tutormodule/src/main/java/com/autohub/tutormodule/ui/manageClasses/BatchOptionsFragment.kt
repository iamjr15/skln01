package com.autohub.tutormodule.ui.manageClasses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorBatchOptionsBinding
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener


class BatchOptionsFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorBatchOptionsBinding
    private lateinit var homeListener: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_batch_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorBatchOptionsBinding.bind(view)
        mBinding.callback = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    fun openAttendanceScreen() {
    }

    fun openManageStudentsScreen() {
        homeListener.showAddBatchFragment(false)

    }

    fun openEditScheduleScreen() {
        homeListener.showAddBatchFragment(false)

    }


    companion object {
        fun newInstance(): BatchOptionsFragment = BatchOptionsFragment()
    }
}