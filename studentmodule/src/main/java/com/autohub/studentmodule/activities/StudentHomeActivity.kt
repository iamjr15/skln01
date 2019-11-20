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
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batchRequests.GradeData
import com.autohub.skln.models.batchRequests.SubjectData
import com.autohub.skln.models.batches.BatchRequestViewModel
import com.autohub.skln.utills.AppConstants
import com.autohub.studentmodule.R
import com.autohub.studentmodule.fragments.*
import com.autohub.studentmodule.listners.HomeListners
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_student_home.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vt Netzwelt
 */

class StudentHomeActivity : BaseActivity(), HomeListners {


    lateinit var subjectDataList: ArrayList<SubjectData>
    lateinit var gradesDataList: ArrayList<GradeData>
    var user: UserModel? = null

    override fun onClassRequestSelectListner(requestViewModel: BatchRequestViewModel) {
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.KEY_DATA, requestViewModel)
        fragmentClassRequests.showRequestDetailFragment(bundle)
        setStatusBarColor(com.autohub.skln.R.drawable.bg_purple_blue)
    }


    override fun onAcadmicsSelect(user: UserModel, subjectName: String) {

        if (explorebaseFragment.exploreTutorsFragment == null) {
            explorebaseFragment.showExploreFragment(subjectName)

        } else {
            explorebaseFragment.exploreTutorsFragment?.updateExploreData(user, subjectName)

        }
        mViewPager!!.currentItem = 2

    }

    private val mTabs = ArrayList<TextView>()
    private var mViewPager: ViewPager? = null
    private lateinit var explorebaseFragment: ExploreBaseFragment
    private lateinit var fragmentClassRequests: MyRequestBaseFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        setStatusBarColor(R.drawable.white_header)
        showLoading()
        getAppData()
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

    }

    private fun updateExploreViews() {
        explorebaseFragment.showExploreFragment()

    }


    private fun getUserInfo() {
        firebaseStore.collection(getString(R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    user = documentSnapshot.toObject(UserModel::class.java)!!
                    val geopoints = ((documentSnapshot.data!!.get("personInfo") as HashMap<*, *>).get("location")) as GeoPoint
                    user!!.personInfo!!.latitude = geopoints.latitude
                    user!!.personInfo!!.longitude = geopoints.longitude
                    user = user
                    user!!.id = documentSnapshot.id
                    hideLoading()

                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }


    private fun getAppData() {
        gradesDataList = ArrayList()
        subjectDataList = ArrayList()
        firebaseStore.collection("subjects").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(SubjectData::class.java)
                    subjectDataList.add(user)
                }


                firebaseStore.collection("grades").get().addOnCompleteListener {

                    if (it.isSuccessful) {
                        for (document in it.result!!) {
                            val user = document.toObject(GradeData::class.java)
                            gradesDataList.add(user)
                            getUserInfo()
                        }
                    }
                }
            }
        }


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


            fragmentClassRequests.showRequestFragmentClass()

        }

    }


    companion object {
        const val TUROR_REQUEST = 1001

    }
}
