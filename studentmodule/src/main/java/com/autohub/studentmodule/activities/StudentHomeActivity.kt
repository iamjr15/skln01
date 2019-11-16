package com.autohub.studentmodule.activities

import android.app.Activity
import android.content.Intent
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
import com.autohub.skln.models.RequestViewModel
import com.autohub.skln.models.TutorSubjectsModel
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.AppConstants
import com.autohub.studentmodule.R
import com.autohub.studentmodule.fragments.*
import com.autohub.studentmodule.listners.HomeListners
import kotlinx.android.synthetic.main.activity_student_home.*

/**
 * Created by Vt Netzwelt
 */

class StudentHomeActivity : BaseActivity(), HomeListners {

    override fun onClassRequestSelectListner(requestViewModel: RequestViewModel) {
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.KEY_DATA, requestViewModel)
        fragmentClassRequests.showRequestDetailFragment(bundle)
        setStatusBarColor(com.autohub.skln.R.drawable.bg_purple_blue)
    }


    override fun onAcadmicsSelect(user: UserModel, classname: String) {
        mViewPager!!.currentItem = 2
        // explorebaseFragment.exploreTutorsFragment?.updateExploreData(user, classname)

    }

    private val mTabs = ArrayList<TextView>()
    private var mViewPager: ViewPager? = null
    private lateinit var explorebaseFragment: ExploreBaseFragment
    private lateinit var fragmentClassRequests: MyRequestBaseFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        setStatusBarColor(R.drawable.white_header)
        mTabs.add(tab_item_home)
        mTabs.add(tab_item_toolbox)
        mTabs.add(tab_item_explore)
        mTabs.add(tab_item_cls_request)
        mTabs.add(tab_item_profile)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        mViewPager!!.adapter = sectionsPagerAdapter
        mViewPager!!.offscreenPageLimit = 4
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (explorebaseFragment.exploreTutorsFragment == null) {
                    explorebaseFragment.showExploreFragment()
                }

                if (position == 4) {
                    setStatusBarColor(R.drawable.tutor_profile_header)

                } else {
                    setStatusBarColor(R.drawable.white_header)
                }
                for (i in mTabs.indices) {
                    if (position == i) {
                        mTabs[i].setTextColor(ContextCompat.getColor(this@StudentHomeActivity, R.color.black))
                        setTextViewDrawableColor(mTabs[i], R.color.black)
                    } else {

                        mTabs[i].setTextColor(ContextCompat.getColor(this@StudentHomeActivity, R.color.light_grey))

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

        tab_item_explore.setOnClickListener {
            if (explorebaseFragment.exploreTutorsFragment == null) {
                explorebaseFragment.showExploreFragment()
            }
            setStatusBarColor(R.drawable.white_header)

            mViewPager!!.currentItem = 2
        }


        tab_item_cls_request.setOnClickListener {
            if (fragmentClassRequests.fragmentClassRequests == null) {
                fragmentClassRequests.showRequestFragmentClass()
            }
            setStatusBarColor(R.drawable.white_header)

            mViewPager!!.currentItem = 3
        }

        fetchTutorSubjects()
        fetchTutorGrades()
    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(textView.context, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    /*override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }*/

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return FragmentHome()
                1 -> return MyClassesFragment()
                2 -> {
                    explorebaseFragment = ExploreBaseFragment()
                    return explorebaseFragment
                }
                3 -> {
                    fragmentClassRequests = MyRequestBaseFragment()
                    return fragmentClassRequests
                }
                else -> {
                    val fragmentProfile = StudentProfileFragment()
                    val bundle = Bundle()
                    bundle.putString(AppConstants.KEY_TYPE, "student")
                    fragmentProfile.arguments = bundle
                    return fragmentProfile
                }
            }
        }

        override fun getCount(): Int {
            return 5
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TUROR_REQUEST) {

            if (resultCode == Activity.RESULT_OK && data != null) {
                val bundle = data.extras
                explorebaseFragment.showRequestDetailFragment(bundle!!)
                setStatusBarColor(com.autohub.skln.R.drawable.bg_purple_blue)
            }
        }

    }


    fun fetchTutorSubjects() {
        var tutorsList: ArrayList<TutorSubjectsModel> = ArrayList<TutorSubjectsModel>()


        firebaseStore.collection("tutorGrades").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(TutorSubjectsModel::class.java)
                    tutorsList.add(user)
                }
            }
        }
    }

    fun fetchTutorGrades() {
        var tutorsList: ArrayList<TutorSubjectsModel> = ArrayList<TutorSubjectsModel>()


        firebaseStore.collection("tutorSubjects").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(TutorSubjectsModel::class.java)
                    tutorsList.add(user)
                }
            }
        }
    }


    companion object {
        const val TUROR_REQUEST = 1001

    }
}
