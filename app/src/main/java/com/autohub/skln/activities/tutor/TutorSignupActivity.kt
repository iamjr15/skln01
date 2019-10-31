package com.autohub.skln.activities.tutor

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.R
import com.autohub.skln.activities.LoginActivity
import com.autohub.skln.databinding.ActivityTutorSignupBinding
import com.autohub.skln.utills.ActivityUtils

class TutorSignupActivity : BaseActivity() {
    private var mBinding: ActivityTutorSignupBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_signup)
        mBinding!!.callback = this

        val spannable = SpannableStringBuilder(resources.getString(R.string.step1))
        spannable.setSpan(ForegroundColorSpan(Color.BLUE), 9, 20, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        mBinding!!.txtwebsite.setText(spannable, TextView.BufferType.SPANNABLE)

    }


    fun login() {
        ActivityUtils.launchActivity(this@TutorSignupActivity, LoginActivity::class.java)
        finish()
    }

}
