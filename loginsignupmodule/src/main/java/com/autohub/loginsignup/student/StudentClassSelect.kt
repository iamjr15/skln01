package com.autohub.loginsignup.student

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.ActivityStudentClassSelectBinding
import com.autohub.loginsignup.listners.ClassSelectionListner
import com.autohub.loginsignup.student.fragments.StudentClassFragment
import com.autohub.loginsignup.student.models.Classdata
import com.autohub.loginsignup.utility.Utilities
import com.autohub.skln.BaseActivity
import com.autohub.skln.models.AcadmicsData
import com.autohub.skln.utills.AppConstants.*
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * Created by Vt Netzwelt
 */
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

    private val classesmap = HashMap<String, AcadmicsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_class_select)
        mBinding!!.callback = this

//        Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 1.0f, 2.0f)
        Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 20.0f, 40.0f)


        getGrades()
        insertClassData()

        val countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")
        countList.add("4")
        countList.add("5")
        countList.add("6")
        getFragments(countList)
        val pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 5
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)


    }

    private fun getGrades() {

    }


    private fun getFragments(countList: ArrayList<String>) {

        for (position in countList.indices) {

            when (position) {
                0 -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(0, 2))))
                1 -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(2, 4))))
                2 -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(4, 6))))
                3 -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(6, 8))))
                4 -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(8, 10))))
                else -> fragmentsList.add(StudentClassFragment.newInstance(position, ArrayList(classdatalist.subList(10, 12))))
            }
        }
    }

    private fun insertClassData() {
        classdatalist.add(Classdata(R.color.black, R.drawable.one, false, CLASS_1))
        classdatalist.add(Classdata(R.color.two, R.drawable.two, false, CLASS_2))
        classdatalist.add(Classdata(R.color.three, R.drawable.three, false, CLASS_3))
        classdatalist.add(Classdata(R.color.four, R.drawable.four, false, CLASS_4))
        classdatalist.add(Classdata(R.color.five, R.drawable.five, false, CLASS_5))
        classdatalist.add(Classdata(R.color.six, R.drawable.six, false, CLASS_6))
        classdatalist.add(Classdata(R.color.seven, R.drawable.seven, false, CLASS_7))
        classdatalist.add(Classdata(R.color.eight, R.drawable.eight, false, CLASS_8))
        classdatalist.add(Classdata(R.color.nine, R.drawable.nine, false, CLASS_9))
        classdatalist.add(Classdata(R.color.ten, R.drawable.ten, false, CLASS_10))
        classdatalist.add(Classdata(R.color.eleven, R.drawable.eleven, false, CLASS_11))
        classdatalist.add(Classdata(R.color.tweleve, R.drawable.tweleve, false, CLASS_12))
    }


    inner class PagerAdapter(fragmentManager: FragmentManager, private var fragmentsList: ArrayList<StudentClassFragment>) :
            FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        // 2
        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getPageWidth(position: Int): Float {
            return 0.6f
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

        firebaseStore.collection("grades").whereEqualTo("grade", selectedClass).get().addOnSuccessListener {

            it.forEach {
                updatedataOnFirebase(it.getString("id")!!)
            }
        }


    }


    /*
    * add user slected class id on firebase
    * */
    fun updatedataOnFirebase(selectedclassId: String) {

        val isSeniorClass = selectedClass.equals(CLASS_11, ignoreCase = true) || selectedClass.equals(CLASS_12, ignoreCase = true)

        val user = HashMap<String, Any>()
        val uid = firebaseAuth.currentUser!!.uid
        user[KEY_SELECTED_CLASS] = selectedclassId
        user[KEY_USER_ID] = uid
        firebaseStore.collection(getString(R.string.db_root_students)).whereEqualTo(KEY_USER_ID, firebaseAuth.currentUser!!.uid)
                .get().addOnSuccessListener {
                    it.forEach {

                        /*
                        * Save user Document Id locally for future use
                        * */

                        appPreferenceHelper.setUserId(it.id)

                        firebaseStore.collection(getString(R.string.db_root_students)).document(it.id).set(mapOf(
                                KEY_USER_ID to uid,
                                KEY_ACADEMICINFO to mapOf(KEY_SELECTED_CLASS to selectedclassId)
                        ), SetOptions.merge())
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

    }

}
