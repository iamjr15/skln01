package com.netzwelt.loginsignup

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.netzwelt.loginsignup.student.SignupStart
import com.autohub.skln.utills.ActivityUtils
import com.netzwelt.loginsignup.databinding.ActivityTutororStudentSelectionBinding
/**
 * Created by Vt Netzwelt
 */
class TutororStudentSelection : BaseActivity() {
    private var mBinding: ActivityTutororStudentSelectionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutoror_student_selection)
        mBinding!!.callback = this
    }


    fun onTutorClick() {
        ActivityUtils.launchActivity(this@TutororStudentSelection, TutorSignupActivity::class.java)
    }

    fun onStudentClick() {
        ActivityUtils.launchActivity(this@TutororStudentSelection, SignupStart::class.java)
    }
}
