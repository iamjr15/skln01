package com.autohub.skln.activities.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autohub.skln.BaseActivity
import com.autohub.skln.R
import com.autohub.skln.activities.user.fragments.StudentClassFragment
import com.autohub.skln.databinding.ActivityStudentClassSelectBinding
import com.autohub.skln.listeners.ClassSelectionListner
import com.autohub.skln.models.Classdata
import com.autohub.skln.utills.AppConstants.*
import com.google.firebase.firestore.SetOptions
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.indices
import kotlin.collections.set


class StudentClassSelect : BaseActivity(), ClassSelectionListner {
    override fun selectedClass(position: Int, isSecondSelected: Boolean, selectedClass: String) {
        this.selectedClass = selectedClass
        for (i in fragmentsList.indices) {
            if (position != i) {
                fragmentsList[i].updateFragment(true)
            } else {
                fragmentsList[i].updateFragment(isSecondSelect = isSecondSelected)
            }
        }
    }


    private var mBinding: ActivityStudentClassSelectBinding? = null
    private var classdatalist: ArrayList<Classdata> = ArrayList()
    private var fragmentsList: ArrayList<StudentClassFragment> = ArrayList()
    private var selectedClass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_class_select)
        mBinding!!.callback = this
        insertClassData()

        var countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")
        countList.add("4")
        countList.add("5")
        countList.add("6")
        getFragments(countList)
        var pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 5
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)


    }

    private fun getFragments(countList: ArrayList<String>) {

        for (position in countList.indices) {

            if (position == 0) {
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(0, 2))))

            } else if (position == 1)
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(2, 4))))
            else if (position == 2)
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(4, 6))))
            else if (position == 3)
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(6, 8))))
            else if (position == 4)
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(8, 10))))
            else
                fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(10, 12))))
        }
    }

    private fun insertClassData() {
        classdatalist.add(Classdata(R.color.black, R.drawable.one, false, CLASS_1))
        classdatalist.add(Classdata(R.color.two, R.drawable.two, false, CLASS_2))
        classdatalist.add(Classdata(R.color.three, R.drawable.three, false, CLASS_3))
        classdatalist.add(Classdata(R.color.four, R.drawable.four, false, CLASS_4))
        classdatalist.add(Classdata(R.color.five, R.drawable.one, false, CLASS_5))
        classdatalist.add(Classdata(R.color.six, R.drawable.one, false, CLASS_6))
        classdatalist.add(Classdata(R.color.seven, R.drawable.one, false, CLASS_7))
        classdatalist.add(Classdata(R.color.eight, R.drawable.one, false, CLASS_8))
        classdatalist.add(Classdata(R.color.nine, R.drawable.one, false, CLASS_9))
        classdatalist.add(Classdata(R.color.ten, R.drawable.one, false, CLASS_10))
        classdatalist.add(Classdata(R.color.eleven, R.drawable.one, false, CLASS_11))
        classdatalist.add(Classdata(R.color.tweleve, R.drawable.one, false, CLASS_12))
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    inner class PagerAdapter(fragmentManager: FragmentManager, fragmentsList: ArrayList<StudentClassFragment>) :
            FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        // 2
        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]


        }

        override fun getPageWidth(position: Int): Float {
            return 0.5f
        }

        // 3
        override fun getCount(): Int {
            return fragmentsList.size
        }
    }

    fun onNextClick() {

        if (TextUtils.isEmpty(selectedClass)) {
            showSnackError(R.string.select_class_text_1)
            return
        }

        showLoading()

        val isSeniorClass = selectedClass.equals(CLASS_11, ignoreCase = true) || selectedClass.equals(CLASS_12, ignoreCase = true)

        val user = HashMap<String, Any>()
        val uid = firebaseAuth.currentUser!!.uid
        user[KEY_STDT_CLASS] = selectedClass
        user[KEY_USER_ID] = uid

        firebaseStore.collection(getString(R.string.db_root_students)).document(uid).set(user, SetOptions.merge())
                .addOnSuccessListener {
                    hideLoading()
                    val i = Intent(this@StudentClassSelect, StudentHey::class.java)
                    i.putExtra("is_senior", isSeniorClass)
                    startActivity(i)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }
}
