package com.autohub.loginsignup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.databinding.ActivityForgetPasswordBinding
import com.autohub.skln.utills.AppConstants

class ForgetPasswordActivity : AppCompatActivity() {
    private var mBinding: ActivityForgetPasswordBinding? = null
    private val mAccountType = AppConstants.TYPE_STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        mBinding!!.callback = this


    }

    fun onForgotPassword() {


    }

}


/*FirebaseAuth.getInstance().sendPasswordResetEmail("user@example.com")
    .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Log.d(TAG, "Email sent.");
            }
        }
    });*/

