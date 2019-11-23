package com.autohub.loginsignup

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.databinding.ActivityForgetPasswordBinding
import com.autohub.skln.BaseActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Vt Netzwelt
 */

class ForgetPasswordActivity : BaseActivity() {
    private var mBinding: ActivityForgetPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        mBinding!!.callback = this

    }

    fun onForgotPassword() {
        if (validateField()) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(mBinding!!.edtemail.text.toString().trim())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Password Reset Email Send", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        showSnackError(it.message)

                    }

        }

    }


    fun validateField(): Boolean {
        val email = mBinding!!.edtemail.text!!

        if (email == null) {
            mBinding!!.edtemail.error = resources.getString(R.string.enter_email)
            mBinding!!.edtemail.requestFocus()
            return false
        }
        if (!isValidEmailId(email.toString())) {
            mBinding!!.edtemail.error = resources.getString(R.string.enter_validemail)
            mBinding!!.edtemail.requestFocus()
            return false
        }
        return true
    }


}


/**/

