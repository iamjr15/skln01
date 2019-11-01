package com.autohub.skln.activities.user

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.autohub.skln.BaseActivity
import com.autohub.skln.R
import com.autohub.skln.fragment.FragmentClassRequests
import com.autohub.skln.fragment.FragmentProfile
import com.autohub.skln.fragment.FragmentToolbox
import com.autohub.skln.activities.user.fragments.ExploreTutorsFragment
import com.autohub.skln.activities.user.fragments.FragmentHome
import com.autohub.skln.utills.AppConstants
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*


class StudentHomeActivity : BaseActivity() {

    private val mTabs = ArrayList<TextView>()
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        setStatusBarColor(R.drawable.white_header)
        mTabs.add(findViewById<View>(R.id.tab_item_home) as TextView)
        mTabs.add(findViewById<View>(R.id.tab_item_toolbox) as TextView)
        mTabs.add(findViewById<View>(R.id.tab_item_explore) as TextView)
        mTabs.add(findViewById<View>(R.id.tab_item_cls_request) as TextView)
        mTabs.add(findViewById<View>(R.id.tab_item_profile) as TextView)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        mViewPager!!.adapter = sectionsPagerAdapter
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 4) {
                    setStatusBarColor(R.drawable.tutor_profile_header)

                } else {
                    setStatusBarColor(R.drawable.white_header)
                }
                for (i in mTabs.indices) {
                    if (position == i) {
                        mTabs[i].setTextColor(resources.getColor(R.color.black))
                        setTextViewDrawableColor(mTabs[i], R.color.black)
                    } else {
                        mTabs[i].setTextColor(resources.getColor(R.color.light_grey))
                        setTextViewDrawableColor(mTabs[i], R.color.light_grey)
                    }
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        for (tab in mTabs) {
            tab.setOnClickListener { mViewPager!!.currentItem = mTabs.indexOf(tab) }
        }
    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return FragmentHome()
            } else if (position == 1) {
                return FragmentToolbox()
            } else if (position == 2) {
                return ExploreTutorsFragment()
            } else if (position == 3) {
                val bundle = Bundle()
                bundle.putString(AppConstants.KEY_TYPE, "student")
                val fragmentClassRequests = FragmentClassRequests()
                fragmentClassRequests.arguments = bundle
                return fragmentClassRequests
            } else {
                val fragmentProfile = FragmentProfile()
                val bundle = Bundle()
                bundle.putString(AppConstants.KEY_TYPE, "student")
                fragmentProfile.arguments = bundle
                return fragmentProfile
            }
        }

        override fun getCount(): Int {
            return 5
        }
    }
}
