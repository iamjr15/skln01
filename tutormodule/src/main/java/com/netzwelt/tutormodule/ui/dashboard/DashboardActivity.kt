package com.netzwelt.tutormodule.ui.dashboard

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
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.ui.dashboard.home.HomeBaseFragment
import com.netzwelt.tutormodule.ui.dashboard.listner.HomeListener
import com.netzwelt.tutormodule.ui.dashboard.profile.ProfileFragment
import com.netzwelt.tutormodule.ui.dashboard.requests.ClassRequestBaseFragment
import com.netzwelt.tutormodule.ui.dashboard.schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*

class DashboardActivity : BaseActivity(), HomeListener {

    private val mTabs = ArrayList<TextView>()
    private lateinit var mViewPager: ViewPager
    private lateinit var homeBaseFragment: HomeBaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mTabs.add(tab_item_home)
        mTabs.add(tab_item_schedule)
        mTabs.add(tab_item_request)
        mTabs.add(tab_item_profile)
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        mViewPager.adapter = sectionsPagerAdapter
        mViewPager.offscreenPageLimit = 4




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

    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    homeBaseFragment = HomeBaseFragment()
                    return homeBaseFragment


                }
                1 -> return ScheduleFragment()
                2 -> {
                    return ClassRequestBaseFragment()
                }
                else -> {
                    return ProfileFragment()
                }
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }

    override fun showBatchOptionsFragment() {
        homeBaseFragment.showBatchOptionsFragment()
    }

    override fun showAddBatchFragment(showAddBatch : Boolean) {
        homeBaseFragment.showAddBatchFragment(showAddBatch)
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

}
