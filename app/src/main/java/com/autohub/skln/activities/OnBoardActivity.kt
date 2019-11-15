package com.autohub.skln.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.BuildConfig
import com.autohub.skln.R
import com.autohub.skln.databinding.ActivityOnBoardBinding
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

/**
 * Created by Vt Netzwelt
 */

class OnBoardActivity : BaseActivity() {

    private var mBinding: ActivityOnBoardBinding? = null
    private lateinit var manager: SplitInstallManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = SplitInstallManagerFactory.create(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)
        mBinding!!.callback = this
        checkPrefrences()
    }


    private fun checkPrefrences() {
        if (firebaseAuth.currentUser != null) {
            if (appPreferenceHelper.signUpComplete) {
                Intent().setClassName(BuildConfig.APPLICATION_ID, STUDENTHOMEACTIVITY_PATH)
                        .also {

                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }
            } else {

                Intent().setClassName(BuildConfig.APPLICATION_ID, STUDENTCLASSACTIVITY_PATH)
                        .also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }

            }
        }
    }

    fun onLogin() {

        loadAndLaunchModule(LOGIN__FEATURE, getString(R.string.loginsignupfeature))
    }

    fun onSignup() {
        loadAndLaunchModule(LOGIN_SIGNUP_FEATURE, getString(R.string.loginsignupfeature))
    }

    /*
    * Load the Signup Login Module and Lauch the Login in Activity in the Module
    * */
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

        val request = SplitInstallRequest.newBuilder()
                .addModule(feature_name)
                .build()

        manager.startInstall(request).addOnSuccessListener {
            hideLoading()
        }.addOnFailureListener {
            showSnackError(it.message)
            hideLoading()
        }
    }


    companion object {
        private const val LOGIN_SIGNUP_FEATURE = "com.autohub.loginsignup.TutororStudentSelection"
        private const val LOGIN__FEATURE = "com.autohub.loginsignup.LoginActivity"

        private const val STUDENTHOMEACTIVITY_PATH = "com.autohub.studentmodule.activities.StudentHomeActivity"
        private const val STUDENTCLASSACTIVITY_PATH = "com.autohub.loginsignup.student.StudentClassSelect"
    }

}
