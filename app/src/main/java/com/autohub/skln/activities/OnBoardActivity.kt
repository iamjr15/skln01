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
                /*val i = Intent(this@OnBoardActivity, com.netzwelt.studentmodule.StudentHomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()*/

                Intent().setClassName(BuildConfig.APPLICATION_ID, "com.netzwelt.studentmodule.activities.StudentHomeActivity")
                        .also {

                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()
                        }
            } else {
                /*val i = Intent(this@OnBoardActivity, com.netzwelt.loginsignup.student.StudentClassSelect::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()*/
                Intent().setClassName(BuildConfig.APPLICATION_ID, "com.netzwelt.loginsignup.student.StudentClassSelect")

                        .also {

                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            finish()

                        }

            }
        }
    }

    fun onLogin() {

        loadAndLaunchModule(LOGIN__FEATURE, "LoginSignup")
        // ActivityUtils.launchActivity(this@OnBoardActivity, LoginActivity::class.java)
    }

    fun onSignup() {
        loadAndLaunchModule(LOGIN_SIGNUP_FEATURE, "LoginSignup")

        // ActivityUtils.launchActivity(this@OnBoardActivity, TutorOrStudent::class.java)
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
    //com.example.login_signup_feature
    //com.example.login_signup_feature.base.LoginActivity
    //login_signup_feature

    companion object {
        private const val LOGIN_SIGNUP_FEATURE = "com.netzwelt.loginsignup.TutororStudentSelection"
        private const val LOGIN__FEATURE = "com.netzwelt.loginsignup.LoginActivity"
        //  private const val GET_CURRENT_LOCATION_FEATURE = "com.example.location_picker.CurrentLocation"
    }

}
