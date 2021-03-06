package com.autohub.studentmodule.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants.KEY_BATCHCODE
import com.autohub.skln.utills.AppConstants.KEY_BATCHCODES
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ActivityAddClassBinding
import com.autohub.studentmodule.models.BatchesModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

/**
 * Created by Vt Netzwelt
 */

class AddClassActivity : BaseActivity() {
    private var mBinding: ActivityAddClassBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_class)
        mBinding!!.callback = this
    }

    /*
    * BatchCode field validation
    * */
    fun onAddclick() {
        if (mBinding!!.edtcode.text.toString() == "") {
            showSnackError(getString(R.string.addbatchcode))
            return
        }
        showLoading()
        getBatchCodeDetails()


    }

    fun onOkClick() {
        val extras = Bundle()
        extras.putSerializable("isFromAddClass", true)
        ActivityUtils.launchActivity(this, StudentHomeActivity::class.java, extras)
        finishAffinity()


    }

    /*
    * Check whether Batch code exist in Firebase
    * if batch code existed check wether it contains contains user id in enrolledStudentsId field
    * */
    private fun getBatchCodeDetails() {
        val batchCode = mBinding!!.edtcode.text.toString().trim()
        firebaseStore.collection(getString(R.string.db_root_batches)).whereEqualTo(KEY_BATCHCODE
                , batchCode).get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result!!.size() > 0) {
                var batchTitle: String
                var batchCode: String
                for (document in task.result!!) {
                    val batchesModel = document.toObject(BatchesModel::class.java)
                    if (batchesModel.enrolledStudentsId!!.contains(firebaseAuth.currentUser!!.uid)) {
                        batchesModel.documentId = document.id
                        batchTitle = batchesModel.title!!
                        batchCode = batchesModel.batchCode!!

                        mBinding!!.txtadded.text = "you have been added into - $batchTitle Successfully."
                        mBinding!!.lladdclass.visibility = View.GONE
                        mBinding!!.lladdclasssucess.visibility = View.VISIBLE
                        // add batch code as array in your profile for future use

                        addBatchCodeInStudent(batchTitle, batchCode)
                    } else {
                        showSnackError(getString(R.string.youarenotenrolled_msg))
                    }
                }


            } else {
                showSnackError(getString(R.string.nobatchExist_message))

            }
            hideLoading()
        }.addOnFailureListener { e ->
            hideLoading()
            showSnackError(e.message)
        }

    }

    /*
    * Add Batch code in students collection for later use
    * */
    private fun addBatchCodeInStudent(batchTitle: String, batchCode: String) {
        firebaseStore.collection(getString(R.string.db_root_students))
                .document(appPreferenceHelper.getuserID()).set(
                        mapOf(KEY_BATCHCODES to FieldValue.arrayUnion(batchCode)

                        ), SetOptions.merge()).addOnSuccessListener {
                    hideLoading()
                    Toast.makeText(this,
                            "Your have been added into - $batchTitle Successfully.", Toast.LENGTH_SHORT).show()

                }

    }


}
