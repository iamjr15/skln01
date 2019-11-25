package com.autohub.studentmodule.fragments

import android.content.Context
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
import com.autohub.studentmodule.listners.HomeListners
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
    private var isMyRequest = false
    private lateinit var homeListner: HomeListners


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as BaseActivity).setStatusBarColor(R.drawable.purple_header)
        batchRequestViewModel = arguments!!.getParcelable(AppConstants.KEY_DATA)
        isMyRequest = arguments!!.getBoolean("isMyRequest", false)
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


        try {

            GlideApp.with(requireContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(com.autohub.skln.R.drawable.default_pic)
                    .placeholder(com.autohub.skln.R.drawable.default_pic)
                    .into(mBinding!!.profilePicture)
        } catch (e: Exception) {
        }


    }


    private fun fetchTutor() {
        // get Document id from tutor id first
        val root = getString(R.string.db_root_tutors)

        firebaseStore.collection(root).whereEqualTo(AppConstants.KEY_USER_ID, batchRequestViewModel!!.batchRequestModel!!.teacher!!.id)
                .get().addOnSuccessListener {
                    it.forEach {

                        firebaseStore.collection(root).document(it.id).get()
                                .addOnSuccessListener { documentSnapshot ->
                                    val tutor = documentSnapshot.toObject(TutorData::class.java)

                                    mBinding!!.tutorViewModel = TutorViewModel(tutor!!)
                                    mTutor = tutor
                                    loadPicture()
                                    mBinding!!.rate.text = "fees : $${tutor.packageInfo!!.price} / ${tutor.packageInfo!!.rateOption}"
                                }
                                .addOnFailureListener { e ->
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

            homeListner.onClassRequestDeleted(isMyRequest)

        }.addOnFailureListener { hideLoading() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListner = context as HomeListners


    }

    private fun opDialer() {
        val number = "tel:" + mTutor!!.personInfo!!.phone

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(number)
        startActivity(intent)
    }


}
