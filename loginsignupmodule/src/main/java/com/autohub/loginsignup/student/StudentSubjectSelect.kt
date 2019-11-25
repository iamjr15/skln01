package com.autohub.loginsignup.student

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
import com.autohub.SubjectsModel
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.ActivityStudentSubjectSelectBinding
import com.autohub.loginsignup.listners.ClassSelectionListner
import com.autohub.loginsignup.student.adaptor.PagerAdapter
import com.autohub.loginsignup.student.fragments.StudentSubjectSelectFragmnet
import com.autohub.loginsignup.student.models.SubjectsData
import com.autohub.loginsignup.utility.Utilities
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.AppConstants.*
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch


/**
 * Created by Vt Netzwelt
 */
class StudentSubjectSelect : BaseActivity(), ClassSelectionListner {

    private var selectedSubjects: ArrayList<SubjectsModel> = ArrayList()
    private lateinit var subjectDataList: ArrayList<SubjectsModel>


    override fun selectedClass(position: Int, isSecondSelected: Boolean, selectedClass: Any) {


        for (i in fragmentsList.indices) {
            if (position == i) {
                if (isSecondSelected) {
                    if (selectedSubjects.contains(selectedClass)) {
                        selectedSubjects.remove(selectedClass)
                        (fragmentsList[i] as StudentSubjectSelectFragmnet).updateFragment(isSecond = true, selected = false)

                    } else {
                        selectedSubjects.add(selectedClass as SubjectsModel)
                        (fragmentsList[i] as StudentSubjectSelectFragmnet).updateFragment(isSecond = true, selected = true)
                    }

                } else {
                    if (selectedSubjects.contains(selectedClass)) {
                        selectedSubjects.remove(selectedClass)
                        (fragmentsList[i] as StudentSubjectSelectFragmnet).updateFragment(isSecond = false, selected = false)

                    } else {
                        selectedSubjects.add(selectedClass as SubjectsModel)

                        (fragmentsList[i] as StudentSubjectSelectFragmnet).updateFragment(isSecond = false, selected = true)

                    }
                }


            }
        }
    }

    private var mFavoriteOrLeast: Boolean = false


    private var mBinding: ActivityStudentSubjectSelectBinding? = null
    private var subjectsDataMap: HashMap<String, SubjectsData> = HashMap()
    private var fragmentsList: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, com.autohub.loginsignup.R.layout.activity_student_subject_select)
        mBinding!!.callback = this
        selectedSubjects = ArrayList()
        fetchSubjects()


    }

    private fun fetchSubjects() {
        val listData: ArrayList<SubjectsModel> = arrayListOf()
        firebaseStore.collection("subjects").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(SubjectsModel::class.java)
                    listData.add(user)
                }
            }
            insertSubjectData(listData)

        }.addOnFailureListener {
            showSnackError(it.message)
        }
    }


    private fun getFragments(countList: ArrayList<String>) {


        for (position in countList.indices) {

            when (position) {
                0 -> fragmentsList.add(StudentSubjectSelectFragmnet.newInstance(position, ArrayList(subjectDataList.subList(0, 2))))
                1 -> fragmentsList.add(StudentSubjectSelectFragmnet.newInstance(position, ArrayList(subjectDataList.subList(2, 4))))
                else -> fragmentsList.add(StudentSubjectSelectFragmnet.newInstance(position, ArrayList(subjectDataList.subList(4, 6))))
            }
        }
    }

    private fun insertSubjectData(listData: ArrayList<SubjectsModel>) {

        subjectDataList = ArrayList()

        subjectsDataMap[SUBJECT_SCIENCE] = SubjectsData(R.color.science, com.autohub.skln.R.drawable.microscope, false, SUBJECT_SCIENCE)
        subjectsDataMap[SUBJECT_ENGLISH] = SubjectsData(R.color.english, com.autohub.skln.R.drawable.noun, false, SUBJECT_ENGLISH)
        subjectsDataMap[SUBJECT_MATHS] = SubjectsData(R.color.math, com.autohub.skln.R.drawable.geometry, false, SUBJECT_MATHS)
        subjectsDataMap[SUBJECT_SOCIAL_STUDIES] = SubjectsData(R.color.socialstudies, com.autohub.skln.R.drawable.strike, false, SUBJECT_SOCIAL_STUDIES)
        subjectsDataMap[SUBJECT_LANGUAGES] = SubjectsData(R.color.language, com.autohub.skln.R.drawable.language, false, SUBJECT_LANGUAGES)
        subjectsDataMap[SUBJECT_COMPUTER_SCIENCE] = SubjectsData(R.color.computerscience, com.autohub.skln.R.drawable.informatic, false, SUBJECT_COMPUTER_SCIENCE)


        for (i in listData.indices) {

            if (subjectsDataMap.containsKey(listData[i].name)) {
                val data = subjectsDataMap[listData[i].name]
                listData[i].bloccolor = data!!.color
                listData[i].icon = data.icon
                listData[i].selected = data.selected

                subjectDataList.add(listData[i])
            }
        }
        showView()

    }


    private fun showView() {
        val countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")

        mFavoriteOrLeast = intent.getBooleanExtra("favorite_or_least", true)

        if (mFavoriteOrLeast) {
            val spannable = SpannableStringBuilder(resources.getString(R.string.select_favorite_subject))
            spannable.setSpan(ForegroundColorSpan(Color.BLUE), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spannable.setSpan(UnderlineSpan(), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            mBinding!!.tvSelectText.setText(spannable, TextView.BufferType.SPANNABLE)
            Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 40.0f, 60.0f)

        } else {
            Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 60.0f, 80.0f)

            val spannable = SpannableStringBuilder(resources.getString(R.string.select_least_favorite_subject))
            spannable.setSpan(ForegroundColorSpan(Color.RED), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spannable.setSpan(UnderlineSpan(), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            mBinding!!.tvSelectText.setText(spannable, TextView.BufferType.SPANNABLE)
        }


        getFragments(countList)
        val pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 2
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)
    }


    fun onNextClick() {
        if (selectedSubjects.size == 0) {
            if (mFavoriteOrLeast) {
                showSnackError(R.string.select_favorite_subject)

            } else {
                showSnackError(R.string.select_least_favorite_subject)

            }

            return
        }


        showLoading()

        val batch: WriteBatch = firebaseStore.batch()
        for (i in selectedSubjects) {
            val map: HashMap<String, String> = HashMap()

            if (mFavoriteOrLeast) {
                map["category"] = "favorite"
                map["isFavourite"] = "true"
                map["isLeastFavourite"] = "false"
                map["isHobby"] = "false"
            } else {
                map["category"] = "leastfavorite"

                map["isFavourite"] = "false"
                map["isLeastFavourite"] = "true"
                map["isHobby"] = "false"
            }


            //hdsjhsa

            map["id"] = firebaseAuth.currentUser!!.uid + "_" + i.id
            map["studentId"] = firebaseAuth.currentUser!!.uid
            map["subjectId"] = i.id!!
            map["subjectName"] = i.name!!
            map["subjectQuestion"] = ""
            map["answer"] = ""


            val nycRef = firebaseStore.collection("studentSubjects").document()
            batch.set(nycRef, map)

        }

        batch.commit().addOnCompleteListener {
            if (it.isSuccessful) {
                saveData()
            }

        }.addOnFailureListener {

            showSnackError(it.message)
        }
    }


    private fun saveData() {
        val stringBuilder = StringBuilder()
        if (selectedSubjects.size > 0) {
            stringBuilder.append(selectedSubjects[0].id)
            for (i in 1 until selectedSubjects.size) {
                stringBuilder.append(", ").append(selectedSubjects[i].id)
            }
        }


        if (stringBuilder.isEmpty()) {
            showSnackError(R.string.choose_subjects)
            return
        }
        val user = HashMap<String, Any>()
        if (mFavoriteOrLeast)
            user[KEY_STDT_FAVORITE_CLASSES] = stringBuilder.toString().split(",")
        else
            user[KEY_STDT_LEAST_FAV_CLASSES] = stringBuilder.toString().split(",")

        firebaseStore.collection(getString(R.string.db_root_students)).document(appPreferenceHelper.getuserID()).set(
                mapOf(
                        KEY_ACADEMICINFO to user
                )
                , SetOptions.merge())
                .addOnSuccessListener {
                    hideLoading()
                    val i: Intent
                    if (mFavoriteOrLeast) {
                        i = Intent(this@StudentSubjectSelect, StudentSubjectSelect::class.java)
                        i.putExtra("favorite_or_least", false)
                    } else {
                        i = Intent(this@StudentSubjectSelect, StudentHobbySelect::class.java)
                    }
                    startActivity(i)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }

    }


}
