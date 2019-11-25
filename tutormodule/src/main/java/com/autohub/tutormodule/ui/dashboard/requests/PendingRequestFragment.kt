package com.autohub.tutormodule.ui.dashboard.requests


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batches.BatchRequestModel
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.GlideApp
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentPendingRequestBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy


/**
 * A simple [Fragment] subclass.
 */

class PendingRequestFragment : BaseFragment() {
    private lateinit var mBinding: FragmentPendingRequestBinding
    private lateinit var studentData: UserModel
    private lateinit var tutorData: TutorData
    private val selectedBatch = ArrayList<String>()
    private val batchNames = ArrayList<String>()
    private lateinit var selectedBatchId: String
    private var selectedPosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentPendingRequestBinding.bind(view)

        fetchStudentData()

        /* Open dropdown on the click of accept button showing list of batches */
        mBinding.addBatch.setOnClickListener {
            showSingleSelectionDialog(batchNames, mBinding.addBatch, getString(R.string.select_batch), selectedBatch)
        }

        mBinding.acceptRequest.setOnClickListener {
            acceptRequest()
        }

        mBinding.deleteRequest.setOnClickListener {
            deleteRequest()
        }

        mBinding.contactStudent.isEnabled = false
        mBinding.contactStudent.setOnClickListener {
            if (studentData.personInfo?.phoneNumber?.isEmpty()!!) {
                showSnackError("No Contact Number Available!!")
            } else {
                requestPermissions()

            }
        }
    }

    /* Check if CALL_PHONE permission is granted.if not, show alert */
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            showSnackError("You need to grant phone call permission first!!")
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + studentData.personInfo?.phoneNumber)
            startActivity(callIntent)
        }
    }


    private fun deleteRequest() {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_batch_requests)).document(arguments?.getString("documentId")!!)
                .update("status", "rejected")
                .addOnSuccessListener {
                    hideLoading()
                    Log.d(" ", "DocumentSnapshot successfully updated!")
                    showSnackError("Your request is rejected successfully!!")
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun acceptRequest() {
        if (selectedPosition == -1) {
            showSnackError("You need to add student to batch first!!")
        } else {
            showLoading()
            firebaseStore.collection(getString(R.string.db_root_batch_requests)).document(arguments?.getString("documentId")!!)
                    .update("status", "accepted")
                    .addOnSuccessListener {
                        Log.d(" ", "DocumentSnapshot successfully updated!")
                        fetchBatches(true)
                        mBinding.contactStudent.isEnabled = true
                    }
                    .addOnFailureListener { e ->
                        hideLoading()
                        showSnackError(e.message)
                    }
        }
    }

    /* Fetch list of batches and add in a dropdown. */
    private fun fetchBatches(enrollStudent: Boolean) {
        firebaseStore.collection(getString(R.string.db_root_batches)).whereEqualTo("teacher.id", tutorData.id).get()
                .addOnSuccessListener { documentSnapshot ->
                    val batches = documentSnapshot.toObjects(BatchRequestModel::class.java)
                    for (i in 0 until batches.size) {
                        batches[i].documentId = documentSnapshot.documents[i].id
                        batchNames.add(batches[i].title!!)
                    }

                    if (enrollStudent) {
                        enrollStudentToBatch(batches[selectedPosition])
                    }
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    /* Enroll student in the selected batch. */
    private fun enrollStudentToBatch(selectedBatch: BatchRequestModel) {

        firebaseStore.collection(getString(R.string.db_root_batches)).document(selectedBatch.documentId!!)
                .get().addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val data = documentSnapshot.toObject(BatchesModel::class.java)
                    data?.enrolledStudentsId?.add(arguments?.getString("studentId")!!)
                    firebaseStore.collection(getString(R.string.db_root_batches)).document(selectedBatch.documentId!!).update("enrolledStudentsId", data?.enrolledStudentsId)
                            .addOnSuccessListener {
                                Log.d("success", "enrollStudentToBatch")
                                showSnackError("Your request is accepted successfully!!")
                            }.addOnFailureListener { e ->
                                showSnackError(e.message)
                            }
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }

    }

    private fun fetchStudentData() {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_students)).whereEqualTo("id", arguments?.getString("studentId")).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObjects(UserModel::class.java)
                    if (data.size > 0) {
                        studentData = data[0]
                        fetchTutorData()
                    } else {
                        hideLoading()
                    }

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun fetchTutorData() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                    fetchBatches(false)
                    fillData()
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun fillData() {
        mBinding.name.text = studentData.personInfo?.firstName + " " + studentData.personInfo?.lastName
        mBinding.subject.text = tutorData.qualification?.qualificationArea
        mBinding.classType.text = tutorData.qualification?.classType?.joinToString(", ")
        mBinding.fees.text = "FEES : \$" + tutorData.packageInfo?.price + " / MONTH"
        mBinding.paymentMethod.text = "PAYMENT METHOD : " + tutorData.packageInfo?.paymentType

        GlideApp.with(this)
                .load(studentData.personInfo!!.accountPicture)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mBinding.profilePicture)
    }


    private fun showSingleSelectionDialog(items: ArrayList<String>, textView: TextView, title: String, selectedItems: ArrayList<String>) {
        val namesArr = items.toTypedArray()
        var indexSelected = -1
        if (selectedItems.size > 0) {
            for (i in namesArr.indices) {
                if (namesArr[i].equals(selectedItems[0])) {
                    indexSelected = i
                    break
                } else {
                    indexSelected = 0
                }
            }
        } else {
            indexSelected = 0

        }


        AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(namesArr, indexSelected, null)
                .setTitle(title)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedPosition < 0) {
                        selectedPosition = 0
                    }
                    textView.text = "Added to " + namesArr[selectedPosition]
                    selectedItems.clear()
                    selectedItems.add(namesArr[selectedPosition])
                }
                .show()
    }

}