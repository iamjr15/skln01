package com.autohub.loginsignup.student

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autohub.SubjectsModel
import com.autohub.loginsignup.LoginActivity
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.ActivityStudentHobbySelectBinding
import com.autohub.loginsignup.listners.ClassSelectionListner
import com.autohub.loginsignup.student.fragments.HobbiesFragment
import com.autohub.loginsignup.student.models.SubjectsData
import com.autohub.loginsignup.utility.Utilities
import com.autohub.skln.BaseActivity
import com.autohub.skln.BuildConfig
import com.autohub.skln.utills.AppConstants.*
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import java.util.*

/**
 * Created by Vt Netzwelt
 */
class StudentHobbySelect : BaseActivity(), ClassSelectionListner {
    private lateinit var manager: SplitInstallManager
    lateinit var hobbyDataList: ArrayList<SubjectsModel>


    override fun selectedClass(position: Int, isSecondSelected: Boolean, selectedClass: String) {
        for (i in fragmentsList.indices) {
            if (position == i) {
                if (isSecondSelected) {
                    if (selectedHobbies.contains(selectedClass)) {
                        selectedHobbies.remove(selectedClass)
                        fragmentsList[i].updateFragment(isSecond = true, selected = false)

                    } else {
                        selectedHobbies.add(selectedClass)

                        fragmentsList[i].updateFragment(isSecond = true, selected = true)

                    }

                } else {
                    if (selectedHobbies.contains(selectedClass)) {
                        selectedHobbies.remove(selectedClass)
                        fragmentsList[i].updateFragment(isSecond = false, selected = false)

                    } else {
                        selectedHobbies.add(selectedClass)

                        fragmentsList[i].updateFragment(isSecond = false, selected = true)

                    }
                }


            }
        }
    }


    private fun fetchobbies() {
        var listData: ArrayList<SubjectsModel> = arrayListOf()
        firebaseStore.collection("subjects").whereEqualTo("grades.hobbies", true).get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(SubjectsModel::class.java)
                    listData.add(user)
                }
            }


            //  listData.add(SubjectsModel(id = "jhjshjfbscdf", name = "keyboard"))


            insertHobbiesData(listData)

        }.addOnFailureListener {
            showSnackError(it.message)
        }
    }


    private fun insertHobbiesData(listData: ArrayList<SubjectsModel>) {
        hobbyDataList = ArrayList()

        var hobbiesDataMap: HashMap<String, SubjectsData> = HashMap()

        hobbiesDataMap[HOBBY_GUITAR] = SubjectsData(R.color.guitar, com.autohub.skln.R.drawable.guitar, false, HOBBY_GUITAR)
        hobbiesDataMap[HOBBY_KEYBOARD] = SubjectsData(R.color.keyboard, com.autohub.skln.R.drawable.piano, false, HOBBY_KEYBOARD)
        hobbiesDataMap[HOBBY_MARTIAL] = SubjectsData(R.color.materialarts, com.autohub.skln.R.drawable.attack, false, HOBBY_MARTIAL)
        hobbiesDataMap[HOBBY_DANCE] = SubjectsData(R.color.dance, com.autohub.skln.R.drawable.dancing, false, HOBBY_DANCE)
        hobbiesDataMap[HOBBY_PAINT] = SubjectsData(R.color.painting, com.autohub.skln.R.drawable.brush, false, HOBBY_PAINT)
        hobbiesDataMap[HOBBY_DRUM] = SubjectsData(R.color.drum, com.autohub.skln.R.drawable.drum, false, HOBBY_DRUM)

        for (i in listData.indices) {

            if (hobbiesDataMap.containsKey(listData[i].name)) {
                val data = hobbiesDataMap[listData[i].name]
                listData[i].bloccolor = data!!.color
                listData[i].icon = data.icon
                listData[i].selected = data.selected

                hobbyDataList.add(listData[i])
            }
        }

        showView()
    }

    private var selectedHobbies: ArrayList<String> = ArrayList()
    private var mBinding: ActivityStudentHobbySelectBinding? = null
    private var fragmentsList: ArrayList<HobbiesFragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_hobby_select)
        manager = SplitInstallManagerFactory.create(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_hobby_select)
        mBinding!!.callback = this
        fetchobbies()


    }

    fun showView() {
        val countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")

        getFragments(countList)
        val pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 2
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)


        Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 80.0f, 100.0f)
    }


    private fun loadAndLaunchModule(name: String, feature_name: String) {
        if (manager.installedModules.contains(feature_name)) {
            Intent().setClassName(BuildConfig.APPLICATION_ID, name)
                    .also {
                        startActivity(it)
                        finishAffinity()

                    }
            return
        } else {
            showLoading()
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
                .addModule(feature_name)
                .build()

        // Load and install the requested feature module.
        manager.startInstall(request).addOnSuccessListener {

            hideLoading()
            finishAffinity()

        }.addOnFailureListener {
            showSnackError(it.message)
            hideLoading()
        }
    }

    private fun getFragments(countList: ArrayList<String>) {

        for (position in countList.indices) {

            when (position) {
                0 -> fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList(hobbyDataList.subList(0, 2))))
                1 -> fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList(hobbyDataList.subList(2, 4))))
                else -> fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList(hobbyDataList.subList(4, 6))))
            }
        }
    }


    fun onNextClick() {

        if (selectedHobbies.size == 0) {
            showSnackError(R.string.select_hobby_text_1)

            return
        }


        showLoading()
        var batch: WriteBatch = firebaseStore.batch()
        for (i in selectedHobbies) {
            var map: HashMap<String, String> = HashMap()
            map["category"] = "hobby"
            //hdsjhsa
            map["id"] = firebaseAuth.currentUser!!.uid + "_" + i
            map["studentId"] = firebaseAuth.currentUser!!.uid
            map["subjectId"] = i


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



        saveData()
    }

    fun saveData() {
        val stringBuilder = StringBuilder()
        if (selectedHobbies.size > 0) {
            stringBuilder.append(selectedHobbies[0])
            for (i in 1 until selectedHobbies.size) {
                stringBuilder.append(", ").append(selectedHobbies[i])
            }
        }

        if (stringBuilder.isEmpty()) {
            showSnackError(R.string.choose_hobbies)
            return
        }


        val user = HashMap<String, Any>()
        user[KEY_STDT_HOBBIES] = stringBuilder.toString().split(",")

        val user1 = HashMap<String, Any>()
        user1[KEY_PHONE_NUMBER] = appPreferenceHelper.userPhone


        firebaseStore.collection(getString(R.string.db_root_students)).document(appPreferenceHelper.getuserID()).set(
                mapOf(
                        KEY_ACADEMICINFO to user
                )
                , SetOptions.merge())
                .addOnSuccessListener {
                    firebaseStore.collection(getString(R.string.db_root_all_users)).document().set(user1, SetOptions.merge())


                            .addOnSuccessListener {
                                hideLoading()
                                appPreferenceHelper.setStudentSignupComplete(true)
                                loadAndLaunchModule(LoginActivity.STUDENT_FEATURE, "studentmodule")

                            }
                            .addOnFailureListener { e ->
                                hideLoading()
                                showSnackError(e.message)
                            }
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }

    }


    inner class PagerAdapter(fragmentManager: FragmentManager, private var fragmentsList: ArrayList<HobbiesFragment>) :
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


    /*   override fun attachBaseContext(newBase: Context) {
           super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
       }*/

/*    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    }*/

}
