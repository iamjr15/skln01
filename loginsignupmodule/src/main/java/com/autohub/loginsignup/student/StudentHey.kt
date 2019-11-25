package com.autohub.loginsignup.student

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.ActivityStudentHeyBinding
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME
import java.util.*

/**
 * Created by Vt Netzwelt
 */
class StudentHey : BaseActivity() {

    private var mBinding: ActivityStudentHeyBinding? = null

    private var mIsSeniorClass: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_hey)
        mBinding!!.callback = this
        firebaseStore.collection(getString(R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val firstName = ((documentSnapshot.data!!["personInfo"] as HashMap<*, *>)[KEY_FIRST_NAME]) as String
                    if (firstName != null) {
                        mBinding!!.studentName!!.text = firstName
                    }
                }
                .addOnFailureListener { e -> showSnackError(e.message) }

        mIsSeniorClass = intent.getBooleanExtra("is_senior", false)
    }

    fun onNextClick() {
        val i: Intent = if (mIsSeniorClass)
            Intent(this, StudentSubjectSelectSeniorActivity::class.java)
        else
            Intent(this, StudentSubjectSelect::class.java)

        i.putExtra("favorite_or_least", true)
        startActivity(i)
    }


}
