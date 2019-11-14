package com.netzwelt.tutormodule.ui.manageClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentTutorBatchOptionsBinding
import com.netzwelt.tutormodule.ui.dashboard.classmanager.StudentsListFragment


class BatchOptionsFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorBatchOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_batch_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorBatchOptionsBinding.bind(view)
        mBinding.callback = this
    }

    fun openAttendanceScreen() {
    }

    fun openManageStudentsScreen() {
        val bundle = Bundle()
        bundle.putBoolean("type", false)
        val addBatchFragment = StudentsListFragment()
        addBatchFragment.arguments = bundle
        openFragment(addBatchFragment)
    }

    fun openEditScheduleScreen() {
        val bundle = Bundle()
        bundle.putBoolean("showAddBatch", false)
        val addBatchFragment = AddBatchFragment()
        addBatchFragment.arguments = bundle
        openFragment(addBatchFragment)

    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.batchOptionsContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(): BatchOptionsFragment = BatchOptionsFragment()
    }
}