package com.netzwelt.loginsignup.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autohub.skln.BaseActivity
import com.autohub.skln.BuildConfig
import com.netzwelt.loginsignup.student.fragments.HobbiesFragment
import com.autohub.skln.listeners.ClassSelectionListner
import com.autohub.skln.models.HobbiesData
import com.autohub.skln.utills.AppConstants.*
import com.google.firebase.firestore.SetOptions
import com.netzwelt.loginsignup.databinding.ActivityStudentHobbySelectBinding
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.netzwelt.loginsignup.LoginActivity
import com.netzwelt.loginsignup.R


class StudentHobbySelect : BaseActivity(), ClassSelectionListner {
    private lateinit var manager: SplitInstallManager


    override fun selectedClass(position: Int, isSecondSelected: Boolean, selectedhobby: String) {
        for (i in fragmentsList.indices) {
            if (position == i) {
                if (isSecondSelected) {
                    if (selectedHobbies.contains(selectedhobby)) {
                        selectedHobbies.remove(selectedhobby)
                        fragmentsList[i].updateFragment(isSecond = true, selected = false)

                    } else {
                        selectedHobbies.add(selectedhobby)

                        fragmentsList[i].updateFragment(isSecond = true, selected = true)

                    }

                } else {
                    if (selectedHobbies.contains(selectedhobby)) {
                        selectedHobbies.remove(selectedhobby)
                        fragmentsList[i].updateFragment(isSecond = false, selected = false)

                    } else {
                        selectedHobbies.add(selectedhobby)

                        fragmentsList[i].updateFragment(isSecond = false, selected = true)

                    }
                }


            }
        }
    }


    private var selectedHobbies: ArrayList<String> = ArrayList()
    private var mBinding: ActivityStudentHobbySelectBinding? = null
    private var hobbiesDataList: ArrayList<HobbiesData> = ArrayList()
    private var fragmentsList: ArrayList<HobbiesFragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_hobby_select)
        manager = SplitInstallManagerFactory.create(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_hobby_select)
        mBinding!!.callback = this
        insertHobbiesData()

        var countList: ArrayList<String> = ArrayList()
        countList.add("1")
        countList.add("2")
        countList.add("3")

        getFragments(countList)
        var pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        mBinding!!.viewpager.adapter = pagerAdapter
        mBinding!!.viewpager.offscreenPageLimit = 2
        mBinding!!.wormDotsIndicator.setViewPager(mBinding!!.viewpager)


    }

    private fun loadAndLaunchModule(name: String, feature_name: String) {
        if (manager.installedModules.contains(feature_name)) {
            Intent().setClassName(BuildConfig.APPLICATION_ID, name)
                    .also {
                        startActivity(it)
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
        }.addOnFailureListener {
            showSnackError(it.message)
            hideLoading()
        }
    }

    private fun getFragments(countList: ArrayList<String>) {


        for (position in countList.indices) {

            if (position == 0) {
                fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList<HobbiesData>(hobbiesDataList.subList(0, 2))))

            } else if (position == 1)
                fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList<HobbiesData>(hobbiesDataList.subList(2, 4))))
            else
                fragmentsList.add(HobbiesFragment.newInstance(position, ArrayList<HobbiesData>(hobbiesDataList.subList(4, 6))))
        }
    }

    private fun insertHobbiesData() {


        hobbiesDataList.add(HobbiesData(R.color.guitar, HOBBY_GUITAR, R.drawable.guitar));
        hobbiesDataList.add(HobbiesData(R.color.keyboard, HOBBY_KEYBOARD, R.drawable.piano));
        hobbiesDataList.add(HobbiesData(R.color.materialarts, HOBBY_MARTIAL, R.drawable.attack));

        hobbiesDataList.add(HobbiesData(R.color.dance, HOBBY_DANCE, R.drawable.dancing))
        hobbiesDataList.add(HobbiesData(R.color.painting, HOBBY_PAINT, R.drawable.brush));

        hobbiesDataList.add(HobbiesData(R.color.drum, HOBBY_DRUM, R.drawable.drum));

    }

    fun onNextClick() {
        val stringBuilder = StringBuilder()
        if (selectedHobbies!!.size > 0) {
            stringBuilder.append(selectedHobbies!![0])
            for (i in 1 until selectedHobbies!!.size) {
                stringBuilder.append(", ").append(selectedHobbies!![i])
            }
        }

        if (stringBuilder.length == 0) {
            showSnackError(R.string.choose_hobbies)
            return
        }

        showLoading()

        val user = HashMap<String, Any>()
        user[KEY_STDT_HOBBIES] = stringBuilder.toString()

        val user1 = HashMap<String, Any>()
        user1[KEY_PHONE_NUMBER] = appPreferenceHelper.userPhone

        firebaseStore.collection(getString(R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).set(user, SetOptions.merge())
                .addOnSuccessListener {
                    firebaseStore.collection(getString(R.string.db_root_all_users)).document(firebaseAuth.currentUser!!.uid).set(user1, SetOptions.merge())
                            .addOnSuccessListener {
                                hideLoading()
                                appPreferenceHelper.setSignupComplete(true)
                                /*     val i = Intent(this@StudentHobbySelect, com.netzwelt.studentmodule.StudentHomeActivity::class.java)
                                     i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                     startActivity(i)
                                     finish()*/

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

    inner class PagerAdapter(fragmentManager: FragmentManager, fragmentsList: ArrayList<HobbiesFragment>) :
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))

    }

}