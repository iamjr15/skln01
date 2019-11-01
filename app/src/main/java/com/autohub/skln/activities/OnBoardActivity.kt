package com.autohub.skln.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.R
import com.autohub.skln.activities.user.StudentClassSelect
import com.autohub.skln.activities.user.StudentHomeActivity
import com.autohub.skln.databinding.ActivityOnBoardBinding
import com.autohub.skln.utills.ActivityUtils

class OnBoardActivity : BaseActivity() {

    private var mBinding: ActivityOnBoardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)
        mBinding!!.callback = this
        checkPrefrences()
    }

    private fun checkPrefrences() {
        if (firebaseAuth.currentUser != null) {
            if (appPreferenceHelper.signUpComplete) {
                val i = Intent(this@OnBoardActivity, StudentHomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            } else {
                val i = Intent(this@OnBoardActivity, StudentClassSelect::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()

            }
        }
    }

    fun onLogin() {


        ActivityUtils.launchActivity(this@OnBoardActivity, LoginActivity::class.java)

    }

    fun onSignup() {
        ActivityUtils.launchActivity(this@OnBoardActivity, TutorOrStudent::class.java)

    }


}
