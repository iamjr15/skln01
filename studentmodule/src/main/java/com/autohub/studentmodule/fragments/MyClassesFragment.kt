package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentMyClassesBinding
import java.util.*

/**
 * Created by Vt Netzwelt
 */
class MyClassesFragment : Fragment() {
    private var mBinding: FragmentMyClassesBinding? = null

    lateinit var scheduleFragment: ScheduleFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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


    fun updateSchedules() {

        scheduleFragment.fetchBatches()
    }

}
