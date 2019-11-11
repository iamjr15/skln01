package com.netzwelt.loginsignup.student

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.autohub.skln.BaseActivity
import com.google.firebase.firestore.SetOptions

import java.util.ArrayList
import java.util.HashMap

import com.netzwelt.loginsignup.student.fragments.StudentSubjectSelect_SeniorFragment
import com.netzwelt.loginsignup.listners.ClassSelectionListner
import com.netzwelt.loginsignup.student.models.SubjectsData
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import com.autohub.skln.utills.AppConstants.KEY_STDT_FAVORITE_CLASSES
import com.autohub.skln.utills.AppConstants.KEY_STDT_LEAST_FAV_CLASSES
import com.autohub.skln.utills.AppConstants.SUBJECT_ACCOUNTANCY
import com.autohub.skln.utills.AppConstants.SUBJECT_BIOLOGY
import com.autohub.skln.utills.AppConstants.SUBJECT_BUSINESS
import com.autohub.skln.utills.AppConstants.SUBJECT_CHEMISTRY
import com.autohub.skln.utills.AppConstants.SUBJECT_COMPUTER_SCIENCE
import com.autohub.skln.utills.AppConstants.SUBJECT_ECONOMICS
import com.autohub.skln.utills.AppConstants.SUBJECT_ENGLISH
import com.autohub.skln.utills.AppConstants.SUBJECT_MATHS
import com.autohub.skln.utills.AppConstants.SUBJECT_PHYSICS
import com.netzwelt.loginsignup.R
import com.netzwelt.loginsignup.databinding.ActivityStudentSubjectSeniorBinding
import com.netzwelt.loginsignup.utility.Utilities

class StudentSubjectSelect_Senior : BaseActivity(), ClassSelectionListner {

    private var selectedSubjects: ArrayList<String> = ArrayList()

    override fun selectedClass(position: Int, isSecondSelected: Boolean, selectedSubject: String) {


        for (i in fragmentsList.indices) {
            if (position == i) {
                if (isSecondSelected) {
                    if (selectedSubjects.contains(selectedSubject)) {
                        selectedSubjects.remove(selectedSubject)
                        fragmentsList[i].updateFragment(isSecond = true, selected = false)

                    } else {
                        selectedSubjects.add(selectedSubject)

                        fragmentsList[i].updateFragment(isSecond = true, selected = true)

                    }

                } else {
                    if (selectedSubjects.contains(selectedSubject)) {
                        selectedSubjects.remove(selectedSubject)
                        fragmentsList[i].updateFragment(isSecond = false, selected = false)

                    } else {
                        selectedSubjects.add(selectedSubject)

                        fragmentsList[i].updateFragment(isSecond = false, selected = true)

                    }
                }


            }
        }
    }


    private var mFavoriteOrLeast: Boolean = false

    private var mBinding: ActivityStudentSubjectSeniorBinding? = null
    private var subjectsDataList: ArrayList<SubjectsData> = ArrayList()
    private var fragmentsList: ArrayList<StudentSubjectSelect_SeniorFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_subject_senior)
        mBinding!!.callback = this
        selectedSubjects = ArrayList()
        insertSubjectData()
        var countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")
        countList.add("4")
        countList.add("5")


        mFavoriteOrLeast = intent.getBooleanExtra("favorite_or_least", true)

        if (mFavoriteOrLeast) {
            val spannable = SpannableStringBuilder(resources.getString(R.string.select_favorite_subject))
            spannable.setSpan(ForegroundColorSpan(Color.BLUE), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spannable.setSpan(UnderlineSpan(), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            mBinding!!.tvSelectText!!.setText(spannable, TextView.BufferType.SPANNABLE)
            Utilities.animateProgressbar(mBinding!!.pbSignupProgress,40.0f,60.0f)

        } else {
            Utilities.animateProgressbar(mBinding!!.pbSignupProgress,60.0f,80.0f)

            val spannable = SpannableStringBuilder(resources.getString(R.string.select_least_favorite_subject))
            spannable.setSpan(ForegroundColorSpan(Color.RED), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spannable.setSpan(UnderlineSpan(), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            mBinding!!.tvSelectText!!.setText(spannable, TextView.BufferType.SPANNABLE)
        }

        getFragments(countList)
        var pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 4
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)
    }

    private fun getFragments(countList: ArrayList<String>) {


        for (position in countList.indices) {

            if (position == 0) {
                fragmentsList.add(StudentSubjectSelect_SeniorFragment.newInstance(position, ArrayList<SubjectsData>(subjectsDataList.subList(0, 2))))

            } else if (position == 1)
                fragmentsList.add(StudentSubjectSelect_SeniorFragment.newInstance(position, ArrayList<SubjectsData>(subjectsDataList.subList(2, 4))))
            else if (position == 2)
                fragmentsList.add(StudentSubjectSelect_SeniorFragment.newInstance(position, ArrayList<SubjectsData>(subjectsDataList.subList(4, 6))))
            else if (position == 3)
                fragmentsList.add(StudentSubjectSelect_SeniorFragment.newInstance(position, ArrayList<SubjectsData>(subjectsDataList.subList(6, 8))))
            else
                fragmentsList.add(StudentSubjectSelect_SeniorFragment.newInstance(position, ArrayList<SubjectsData>(subjectsDataList.subList(8, 9))))
        }
    }

    private fun insertSubjectData() {
        subjectsDataList.add(SubjectsData(R.color.math, R.drawable.geometry, false, SUBJECT_MATHS))
        subjectsDataList.add(SubjectsData(R.color.english, R.drawable.noun, false, SUBJECT_ENGLISH))
        subjectsDataList.add(SubjectsData(R.color.physics, R.drawable.physics, false, SUBJECT_PHYSICS))
        subjectsDataList.add(SubjectsData(R.color.account, R.drawable.accounting, false, SUBJECT_ACCOUNTANCY))
        subjectsDataList.add(SubjectsData(R.color.biology, R.drawable.microscope, false, SUBJECT_BIOLOGY))
        subjectsDataList.add(SubjectsData(R.color.computerscience, R.drawable.informatic, false, SUBJECT_COMPUTER_SCIENCE))
        subjectsDataList.add(SubjectsData(R.color.chemistry, R.drawable.biology, false, SUBJECT_CHEMISTRY))
        subjectsDataList.add(SubjectsData(R.color.economics, R.drawable.rating, false, SUBJECT_ECONOMICS))
        subjectsDataList.add(SubjectsData(R.color.business, R.drawable.rupee, false, SUBJECT_BUSINESS))


    }

    fun onNextClick() {
        val stringBuilder = StringBuilder()
        if (selectedSubjects.size > 0) {
            stringBuilder.append(selectedSubjects[0])
            for (i in 1 until selectedSubjects.size) {
                stringBuilder.append(", ").append(selectedSubjects[i])
            }
        }

        if (stringBuilder.length == 0) {
            showSnackError(R.string.choose_subjects)
            return
        }

        showLoading()

        val user = HashMap<String, Any>()
        if (mFavoriteOrLeast)
            user[KEY_STDT_FAVORITE_CLASSES] = stringBuilder.toString()
        else
            user[KEY_STDT_LEAST_FAV_CLASSES] = stringBuilder.toString()

        firebaseStore.collection(getString(R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).set(user, SetOptions.merge())
                .addOnSuccessListener {
                    hideLoading()
                    val i: Intent
                    if (mFavoriteOrLeast) {
                        i = Intent(this@StudentSubjectSelect_Senior, StudentSubjectSelect_Senior::class.java)
                        i.putExtra("favorite_or_least", false)
                    } else {
                        i = Intent(this@StudentSubjectSelect_Senior, StudentHobbySelect::class.java)
                    }
                    startActivity(i)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }


    inner class PagerAdapter(fragmentManager: FragmentManager, fragmentsList: ArrayList<StudentSubjectSelect_SeniorFragment>) :
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}
