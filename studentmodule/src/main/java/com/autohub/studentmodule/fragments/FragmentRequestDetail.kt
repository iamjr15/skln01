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
import com.autohub.skln.models.RequestViewModel
import com.autohub.skln.models.User
import com.autohub.skln.models.UserViewModel
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.FragmentRequestDetailBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * Created by Vt Netzwelt
 */

class FragmentRequestDetail : BaseFragment() {
    private var mRequestViewModel: RequestViewModel? = null
    private var mBinding: FragmentRequestDetailBinding? = null
    private var mStudent: User? = null
    private var mTutor: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as BaseActivity).setStatusBarColor(R.drawable.purple_header)
        mRequestViewModel = arguments!!.getParcelable(AppConstants.KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentRequestDetailBinding.bind(view)
        mBinding!!.requestViewModel = mRequestViewModel
        mBinding!!.deleteRequest.setOnClickListener {
            var status = Request.STATUS.DELETED.value
            if (mRequestViewModel!!.userType.equals("student", ignoreCase = true)) {
                status = Request.STATUS.CANCELED.value
            }
            changeStatus(status)
        }
        fetchStudent()
        fetchTutor()

        setUpUi()
        mBinding!!.contactStd.setOnClickListener { opDialer() }
        mBinding!!.addBatch.setOnClickListener { openMap() }
        if (mRequestViewModel!!.request.requestStatus == Request.STATUS.PENDING.value) {
            mBinding!!.contactStd.isEnabled = false
        }

    }

    private fun openMap() {
        val uri =
                Uri.parse("geo:<${mTutor?.latitude}>,<${mTutor?.longitude}>?q=<${mTutor?.latitude}>," +
                        "<${mTutor?.longitude}>(${mTutor?.firstName}  + ${mTutor?.lastName} )")


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
        mBinding!!.deleteRequest.isEnabled = !mRequestViewModel!!.request.requestStatus.equals(Request.STATUS.CANCELED.value, ignoreCase = true)
        mBinding!!.deleteRequest.setText(R.string.cancel_request)
        mBinding!!.messageLabel.setText(R.string.request_message_student)
        mBinding!!.requestLogo.setImageResource(R.drawable.ic_request_tick)
        mBinding!!.requestStatus.setText(R.string.new_request_sent)
        mBinding!!.contactStd.setText(R.string.contact_skill_master)
        mBinding!!.addBatch.setText(R.string.see_location_on_map)
        //  }
    }

    private fun loadPicture() {
        if (mStudent == null || mTutor == null) return
        val path = mTutor!!.pictureUrl
        /* if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
            path = mStudent.pictureUrl;
        }*/
        val pathReference1 = FirebaseStorage.getInstance().reference.child(path)
        GlideApp.with(requireContext())
                .load(pathReference1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fallback(com.autohub.skln.R.drawable.default_pic)
                .into(mBinding!!.profilePicture)
    }

    private fun fetchStudent() {
        val root = getString(R.string.db_root_students)
        firebaseStore.collection(root).document(mRequestViewModel!!.request.studentId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val student = documentSnapshot.toObject(User::class.java)
                    if (mRequestViewModel!!.userType.equals("tutor", ignoreCase = true)) {
                        mBinding!!.userViewModel = UserViewModel(student!!)
                    }
                    mStudent = student
                    loadPicture()
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    private fun fetchTutor() {
        val root = getString(R.string.db_root_tutors)
        firebaseStore.collection(root).document(mRequestViewModel!!.request.tutorId).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val tutor = documentSnapshot.toObject(User::class.java)
                    if (mRequestViewModel!!.userType.equals("student", ignoreCase = true)) {
                        mBinding!!.userViewModel = UserViewModel(tutor!!)
                    }
                    mBinding!!.tutorViewModel = UserViewModel(tutor!!)
                    mTutor = tutor
                    loadPicture()
                    mBinding!!.rate.text = "fees : $${tutor!!.rate} / ${tutor.paymentDuration}"
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun changeStatus(requestStatus: String) {
        showLoading()
        val map = HashMap<String, String>()
        map["requestStatus"] = requestStatus
        val dbRoot = getString(R.string.db_root_requests)
        firebaseStore.collection(dbRoot).document(mRequestViewModel!!.requestId).set(map, SetOptions.merge()).addOnSuccessListener {
            hideLoading()
            mRequestViewModel!!.request.requestStatus = requestStatus
            setUpUi()
        }.addOnFailureListener { hideLoading() }
    }

    private fun opDialer() {
        var number = "tel:" + mStudent!!.phoneNumber
        if (mRequestViewModel!!.userType.equals("student", ignoreCase = true)) {
            number = "tel:" + mTutor!!.phoneNumber
        }
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(number)
        startActivity(intent)
    }
}
