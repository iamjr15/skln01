package com.netzwelt.loginsignup.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME
import com.netzwelt.loginsignup.R
import com.netzwelt.loginsignup.databinding.ActivityStudentHeyBinding
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
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
        firebaseStore.collection(getString(R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val firstName = documentSnapshot.getString(KEY_FIRST_NAME)
                    if (firstName != null) {
                        mBinding!!.studentName!!.text = firstName
                    }
                }
                .addOnFailureListener { e -> showSnackError(e.message) }

        mIsSeniorClass = intent.getBooleanExtra("is_senior", false)
    }

    fun onNextClick() {
        val i: Intent
        i = if (mIsSeniorClass)
            Intent(this, StudentSubjectSelectSeniorActivity::class.java)
        else
            Intent(this, StudentSubjectSelect::class.java)

        i.putExtra("favorite_or_least", true)
        startActivity(i)
    }

   /* override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }*/
}
