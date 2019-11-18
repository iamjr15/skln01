package com.autohub.loginsignup

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.databinding.ActivityForgetPasswordBinding
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.AppConstants
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : BaseActivity() {
    private var mBinding: ActivityForgetPasswordBinding? = null
    private val mAccountType = AppConstants.TYPE_STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        mBinding!!.callback = this

    }

    fun onForgotPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail("netzwelt.prithipal@gmail.com")
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showSnackError("EMAIL SEND")
                    }
                }

    }

}


/**/

