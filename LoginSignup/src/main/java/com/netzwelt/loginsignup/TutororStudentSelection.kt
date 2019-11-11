package com.netzwelt.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.netzwelt.loginsignup.student.SignupStart
import com.autohub.skln.utills.ActivityUtils
import com.netzwelt.loginsignup.databinding.ActivityTutororStudentSelectionBinding

class TutororStudentSelection : AppCompatActivity() {
    private var mBinding: ActivityTutororStudentSelectionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutoror_student_selection)
        mBinding!!.callback = this
    }


    fun onTutorClick() {
        ActivityUtils.launchActivity(this@TutororStudentSelection, TutorSignupActivity::class.java)
    }

    // Redirect to the startup of sign up as a Student
    fun onStudentClick() {
        ActivityUtils.launchActivity(this@TutororStudentSelection, SignupStart::class.java)
    }
}