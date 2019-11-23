package com.autohub.tutormodule.ui.dashboard

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.autohub.skln.BaseActivity
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.ui.dashboard.home.HomeBaseFragment
import com.autohub.tutormodule.ui.dashboard.listner.ClassRequestListener
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener
import com.autohub.tutormodule.ui.dashboard.profile.ProfileFragment
import com.autohub.tutormodule.ui.dashboard.requests.RequestBaseFragment
import com.autohub.tutormodule.ui.dashboard.schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*

class DashboardActivity : BaseActivity(), HomeListener, ClassRequestListener {

    private val mTabs = ArrayList<TextView>()
    private lateinit var mViewPager: ViewPager
    private lateinit var homeBaseFragment: HomeBaseFragment
    private lateinit var requestBaseFragment: RequestBaseFragment
    private lateinit var scheduleFragment: ScheduleFragment
    private var profileFragment: ProfileFragment = ProfileFragment()
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mTabs.add(tab_item_home)
        mTabs.add(tab_item_schedule)
        mTabs.add(tab_item_request)
        mTabs.add(tab_item_profile)
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container)
        mViewPager.adapter = sectionsPagerAdapter
        mViewPager.offscreenPageLimit = 3


        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {

                if (position == 3) {
                    setStatusBarColor(R.drawable.tutor_profile_header)
                } else {
                    setStatusBarColor(R.drawable.white_header)
                }

                for (i in mTabs.indices) {
                    if (position == i) {
                        mTabs[i].setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.black))
                        setTextViewDrawableColor(mTabs[i], R.color.black)
                    } else {
                        mTabs[i].setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.light_grey))
                        setTextViewDrawableColor(mTabs[i], R.color.light_grey)
                    }
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })



        for (tab in mTabs) {
            tab.setOnClickListener { mViewPager.currentItem = mTabs.indexOf(tab) }
        }

        tab_item_home.setOnClickListener {
            if (homeBaseFragment.homeFragment == null) {
                homeBaseFragment.showHomefragment()
            }
            setStatusBarColor(R.drawable.white_header)

            mViewPager.currentItem = 0
        }

        tab_item_request.setOnClickListener {
            if (requestBaseFragment.fragmentRequests == null) {
                requestBaseFragment.showRequestFragmentClass()
            }
            setStatusBarColor(R.drawable.white_header)

            mViewPager.currentItem = 2
        }

    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProfile()
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    homeBaseFragment = HomeBaseFragment()
                    return homeBaseFragment
                }
                1 -> {

                    scheduleFragment = ScheduleFragment()
                    return scheduleFragment
                }

                2
                -> {
                    requestBaseFragment = RequestBaseFragment()
                    return requestBaseFragment
                }
                else -> {
                    profileFragment = ProfileFragment()
                    return profileFragment
                }
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }

    override fun showBatchOptionsFragment(batch: BatchesModel) {
        homeBaseFragment.showBatchOptionsFragment(batch)
    }

    override fun showAddBatchFragment(showAddBatch: Boolean, batch: BatchesModel) {
        homeBaseFragment.showAddBatchFragment(showAddBatch, batch)
    }

    override fun showStudentsListFragment() {
        homeBaseFragment.showStudentsListFragment()
    }


    override fun pendingRequestSelect() {
        mViewPager.currentItem = 2

    }

    override fun managerSelected() {
        homeBaseFragment.showManagerFragment()
    }

    override fun showPendingRequestScreen(studentId: String, documentId: String) {
        requestBaseFragment.showPendingRequestScreen(studentId, documentId)
    }

    override fun refreshSchedule() {
        scheduleFragment.fetchTutorData()
    }

    private fun refreshProfile() {
        if (profileFragment != null && profileFragment.isAdded) {
            profileFragment.fetchTutorData()
        }
    }

    override fun onBackPressed() {

        if (mViewPager.currentItem == 0) {

            super.onBackPressed()
        } else {
            mViewPager.currentItem = 0

        }

    }
}
