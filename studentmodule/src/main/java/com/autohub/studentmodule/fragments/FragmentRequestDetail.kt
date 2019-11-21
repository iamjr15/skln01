package com.autohub.studentmodule.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.BaseActivity
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.Request
import com.autohub.skln.models.batches.BatchRequestViewModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentRequestDetailBinding
import com.autohub.studentmodule.models.TutorViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.SetOptions
import java.util.*

/**
 * Created by Vt Netzwelt
 */

class FragmentRequestDetail : BaseFragment() {
    private var batchRequestViewModel: BatchRequestViewModel? = null
    private var mBinding: FragmentRequestDetailBinding? = null
    private var mTutor: TutorData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as BaseActivity).setStatusBarColor(R.drawable.purple_header)
        batchRequestViewModel = arguments!!.getParcelable(AppConstants.KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentRequestDetailBinding.bind(view)
        mBinding!!.requestViewModel = batchRequestViewModel
        mBinding!!.deleteRequest.setOnClickListener {
            var status = Request.STATUS.DELETED.value
            if (batchRequestViewModel!!.mUserType.equals("student", ignoreCase = true)) {
                status = Request.STATUS.CANCELED.value
            }
            changeStatus(status)
        }
        fetchTutor()

        setUpUi()
        mBinding!!.contactStd.setOnClickListener { opDialer() }
        mBinding!!.addBatch.setOnClickListener { openMap() }
        if (batchRequestViewModel!!.batchRequestModel!!.status == Request.STATUS.PENDING.value) {
            mBinding!!.contactStd.isEnabled = false
        }

    }

    private fun openMap() {
        val uri =
                Uri.parse("geo:<${mTutor!!.location!!.latitude}>,<${mTutor!!.location!!.longitude}>?q=<${mTutor?.location!!
                        .latitude}>," +
                        "<${mTutor?.location!!.longitude}>(${mTutor?.personInfo!!.firstName}  + ${mTutor?.personInfo!!.lastName} )")


        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity!!.startActivity(intent)
    }

    private fun setUpUi() {
        /*if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
            mBinding.deleteRequest.setText(R.string.delete_request);
            mBinding.deleteRequest.setEnabled(!mRequestViewModel.getRequest().requestStatus.equalsIgnoreCase(Request.STATUS.DELETED.getValue()));
            mBinding.messageLabel.setText(R.string.request_message);
            mBinding.requestLogo.setImageResource(R.drawable.ic_flash);
            mBinding.requestStatus.setText(R.string.new_request_received);
            mBinding.contactStd.setText(R.string.contact_student);
            mBinding.addBatch.setText(R.string.add_to_batch);
        } else {*/
        mBinding!!.deleteRequest.isEnabled = !batchRequestViewModel!!.batchRequestModel!!.status.equals(Request.STATUS.CANCELED.value, ignoreCase = true)
        mBinding!!.deleteRequest.setText(R.string.cancel_request)
        mBinding!!.messageLabel.setText(R.string.request_message_student)
        mBinding!!.requestLogo.setImageResource(R.drawable.ic_request_tick)
        mBinding!!.requestStatus.setText(R.string.new_request_sent)
        mBinding!!.contactStd.setText(R.string.contact_skill_master)
        mBinding!!.addBatch.setText(R.string.see_location_on_map)
        //  }
    }

    private fun loadPicture() {
        if (mTutor == null) return
        val path = mTutor!!.personInfo!!.accountPicture
        /* if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
            path = mStudent.pictureUrl;
        }*/

        try {

            GlideApp.with(requireContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(com.autohub.skln.R.drawable.default_pic)
                    .into(mBinding!!.profilePicture)
        } catch (e: Exception) {
        }


    }

/*    private fun fetchStudent() {
        val root = getString(R.string.db_root_students)
        firebaseStore.collection(root).document(mRequestViewModel!!.request.studentId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val student = documentSnapshot.toObject(User::class.java)
                    if (mRequestViewModel!!.userType.equals("tutor", ignoreCase = true)) {
                        mBinding!!.userViewModel = UserViewModelold(student!!)
                    }
                    mStudent = student
                    loadPicture()
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }*/

    private fun fetchTutor() {
        // get Document id from tutor id first
        val root = getString(R.string.db_root_tutors)

        firebaseStore.collection(root).whereEqualTo(AppConstants.KEY_USER_ID, batchRequestViewModel!!.batchRequestModel!!.teacher!!.id)
                .get().addOnSuccessListener {
                    it.forEach {

                        firebaseStore.collection(root).document(it.id).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    hideLoading()
                                    val tutor = documentSnapshot.toObject(TutorData::class.java)
                                    /* if (mRequestViewModel!!.userType.equals("student", ignoreCase = true)) {
                                         mBinding!!.userViewModel = TutorViewModel(tutor!!)
                                     }*/
                                    mBinding!!.tutorViewModel = TutorViewModel(tutor!!)
                                    mTutor = tutor
                                    loadPicture()
                                    mBinding!!.rate.text = "fees : $${tutor.packageInfo!!.price} / ${tutor.packageInfo!!.frequency}"
                                }
                                .addOnFailureListener { e ->
                                    hideLoading()
                                    showSnackError(e.message)
                                }
                    }


                }
    }


    private fun changeStatus(requestStatus: String) {
        showLoading()
        val map = HashMap<String, String>()
        map["status"] = requestStatus
        val dbRoot = getString(R.string.db_root_batchRequests)
        firebaseStore.collection(dbRoot).document(batchRequestViewModel!!.mRequestId!!).set(map, SetOptions.merge()).addOnSuccessListener {
            hideLoading()
            // mRequestViewModel!!.request.requestStatus = requestStatus
            setUpUi()
        }.addOnFailureListener { hideLoading() }
    }

    private fun opDialer() {
        var number = "tel:" + mTutor!!.personInfo!!.phone

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(number)
        startActivity(intent)
    }
}
