package com.autohub.tutormodule.ui.dashboard.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.activities.OnBoardActivity
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorProfileBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint

class ProfileFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorProfileBinding
    private var tutorData: TutorData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorProfileBinding.bind(view)

        fetchTutorData()

        mBinding.editProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            if (tutorData != null) {
                intent.putExtra(getString(R.string.containsTutorData), tutorData)
            }
            startActivity(intent)
        }

        mBinding.logout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())

            dialogBuilder.setMessage("Are you sure, you want to logout ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        FirebaseAuth.getInstance().signOut()
                        appPreferenceHelper.tutorSignUpComplete = false

                        ActivityUtils.launchActivity(requireContext(), OnBoardActivity::class.java)
                        requireActivity().finishAffinity()
                    }

                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }

            val alert = dialogBuilder.create()
            alert.setTitle("Log Out")
            alert.show()

        }
    }

    fun fetchTutorData() {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    tutorData = documentSnapshot.toObject(TutorData::class.java)!!

                    val geoPoint = ((documentSnapshot.data?.get("location") as HashMap<*, *>)["geopoint"]) as GeoPoint
                    tutorData!!.location!!.latitude = geoPoint.latitude
                    tutorData!!.location!!.longitude = geoPoint.longitude

                    mBinding.name.text = tutorData?.personInfo?.firstName + " " +
                            tutorData?.personInfo?.lastName?.get(0) + "."

                    if (tutorData?.qualification?.belongToInstitute == true) {
                        mBinding.type.text = tutorData?.qualification?.instituteNeme
                    } else {
                        mBinding.type.text = "Individual tutor"
                    }

                    mBinding.id.text = tutorData?.tutorCode
                    GlideApp.with(this)
                            .load(tutorData!!.personInfo!!.accountPicture)
                            .placeholder(com.autohub.skln.R.drawable.default_pic)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(mBinding.profilePicture)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}