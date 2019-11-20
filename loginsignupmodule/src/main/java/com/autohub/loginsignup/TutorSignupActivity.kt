package com.autohub.loginsignup

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.databinding.ActivityTutorSignupBinding
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.ActivityUtils


/**
 * Created by Vt Netzwelt
 */
class TutorSignupActivity : BaseActivity() {
    private var mBinding: ActivityTutorSignupBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_signup)
        mBinding!!.callback = this

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://skln01.web.app/"))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }

        val spannable = SpannableString(resources.getString(R.string.step1))
        spannable.setSpan(clickableSpan, 9, 20, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        mBinding!!.txtwebsite.setText(spannable, TextView.BufferType.SPANNABLE)
        mBinding!!.txtwebsite.movementMethod = LinkMovementMethod.getInstance()
        mBinding!!.txtwebsite.highlightColor = Color.TRANSPARENT


    }

    fun login() {
        ActivityUtils.launchActivity(this@TutorSignupActivity, LoginActivity::class.java)
        finish()
    }

}
