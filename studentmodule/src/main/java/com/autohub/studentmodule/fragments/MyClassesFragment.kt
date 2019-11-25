package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.utills.ViewPagerAdapter
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentMyClassesBinding

/**
 * Created by Vt Netzwelt
 */
class MyClassesFragment : Fragment() {
    private var mBinding: FragmentMyClassesBinding? = null

    private lateinit var scheduleFragment: ScheduleFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentMyClassesBinding.bind(view)
        val adapter = ViewPagerAdapter(childFragmentManager)
        scheduleFragment = ScheduleFragment()

        adapter.addData(scheduleFragment, "Schedule")
        adapter.addData(EnrolledClassesFragment(), "Enrolled classes")

        mBinding!!.tabs.setupWithViewPager(mBinding!!.viewpager)
        mBinding!!.viewpager.adapter = adapter
    }


    fun updateSchedules() {

        scheduleFragment.fetchBatches()
    }

}
