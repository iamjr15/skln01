package com.autohub.tutormodule.ui.dashboard.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.GlideApp
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorHomeBinding
import com.autohub.tutormodule.ui.dashboard.listener.HomeListener
import com.bumptech.glide.load.engine.DiskCacheStrategy

class HomeFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorHomeBinding
    private lateinit var homeListener: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentTutorHomeBinding.bind(view)
        mBinding.callback = this

        fetchTutorData()
    }

    /**
     * Fetch pending requests data on the basis of teacher id and status(if status equals to pending)
     * @param tutorData
     */
    private fun fetchPendingRequests(tutorData: TutorData) {
        firebaseStore.collection(getString(R.string.db_root_batch_requests))
                .whereEqualTo("teacher.id", tutorData.id)
                .whereEqualTo("status", AppConstants.STATUS_PENDING)
                .get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.documents.size > 0) {
                        val data = documentSnapshot.toObjects(BatchesModel::class.java)
                        if (data != null && data.size > 0) {
                            hideLoading()
                            mBinding.pendingRequestCount.text = data.size.toString()
                        }
                    } else {
                        hideLoading()
                    }
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    /**
     * Fetch Tutor data on the basis of document id and show details
     */
    private fun fetchTutorData() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                    mBinding.name.text = "Hey,\n" + tutorData.personInfo?.firstName

                    GlideApp.with(this)
                            .load(tutorData.personInfo?.accountPicture)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                            .skipMemoryCache(true)
                            .placeholder(com.autohub.skln.R.drawable.default_pic)
                            .into(mBinding.profileImage)
                    fetchPendingRequests(tutorData)

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    fun onPendingRequestClick() {
        homeListener.pendingRequestSelect()

    }

    fun onManageClassClick() {
        homeListener.managerSelected()
    }
}